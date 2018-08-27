# Deploying a JEE app using Websphere Liberty Helm Chart

[Websphere Liberty](https://developer.ibm.com/wasdev/websphere-liberty/) is a Java application server that comes packaged with [Java EE 8 Web profile](http://www.oracle.com/technetwork/java/javaee/overview/compatibility-jsp-136984.html)

IBM provides a [Websphere Liberty Helm Chart](https://github.com/IBM/charts/tree/master/stable/ibm-websphere-liberty/) for easy deployments of Liberty based Java apps in a Kubernetes environment. This recipe will explore ways of deploying a [sample application](https://github.com/pivotal-cf/weblogic-k8s-operator-recipe/tree/master/samples/sample-war-proj) to a PKS environment

## Building a Docker Image
The source code for the sample is available with the PKS recipes [here](https://github.com/pivotal-cf/weblogic-k8s-operator-recipe). 

Clone the main project:

```
git clone https://github.com/pivotal-cf/weblogic-k8s-operator-recipe
# OR
git clone git@github.com:pivotal-cf/weblogic-k8s-operator-recipe.git
```

Move to the samples folder and edit the gradle.properties file with a tag with your [dockerhub](https://hub.docker.com/) username:
```
dockerTag=my-dockerhub-username/sample-war-proj

# eg. 
# dockerTag=bijukunjummen/sample-war-proj
```

Build the project and create a docker image:

```
cd samples/sample-war-proj
./gradlew clean createDockerImage
```

A docker image should have been successfully created at this point:
```
$ docker images
...
REPOSITORY                              TAG                 IMAGE ID            CREATED             SIZE
bijukunjummen/sample-war-proj           0.0.4-SNAPSHOT      55fbfd30e5ca        2 minutes ago       502MB
...
```

Push this image to a docker repository:
```
docker push bijukunjummen/sample-war-proj:0.0.4-SNAPSHOT
```


## Use Websphere Liberty Helm chart to deploy the application

Create a yaml to provide some override values for the Webpshere Liberty helm chart. 

A sample is available in `specs/websphere/libertyOverrides.yml` file:

```
image:
  repository: "bijukunjummen/sample-war-proj"
  tag: "0.0.4-SNAPSHOT"

autoscaling:
  enabled: true
  minReplicas: 2
  maxReplicas: 10
  targetCPUUtilizationPercentage: 40  
```

Install the helm chart:

```
helm repo add ibm-charts https://raw.githubusercontent.com/IBM/charts/master/repo/stable/
helm install ibm-charts/ibm-websphere-liberty --name liberty-boot-app -f libertyOverrides.yml
```

## Make the service visible to an end user

The service deployed by the helm chart is a `NodePort`, for some reason nodeports are not visible to an enduser and need to be changed to a `LoadBalancer` type. 

```
kubectl edit service liberty-boot-app-ibm-web
```

and change the port to 443 and service type to `LoadBalancer`:

```
apiVersion: v1
kind: Service
...
spec:
  ...
  externalTrafficPolicy: Cluster
  ports:
  - name: http-liberty-https
    nodePort: 32299
    port: 443
    protocol: TCP
    targetPort: 9443
  selector:
    app: liberty-boot-app-ibm-web
  sessionAffinity: None
  type: LoadBalancer
```

Grab the ip for the loadbalancer:

```
kubectl get service liberty-boot-app-ibm-web -o 'jsonpath={.status.loadBalancer.ingress[0].ip}'
```

## Test the app
If the deployment has completed, it can be tested using a curl command against the load balancer ip:

```
curl -k  -s https://10.195.52.152/sample-war-proj/ping
```