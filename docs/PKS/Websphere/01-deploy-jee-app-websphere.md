# Deploying a JEE app using Websphere Liberty Helm Chart

[Websphere Liberty](https://developer.ibm.com/wasdev/websphere-liberty/) is a Java application server that comes packaged with [Java EE 8 Web profile](http://www.oracle.com/technetwork/java/javaee/overview/compatibility-jsp-136984.html)

IBM provides a [Websphere Liberty Helm Chart](https://github.com/IBM/charts/tree/master/stable/ibm-websphere-liberty/) for easy deployments of Liberty based Java apps in a Kubernetes environment. This recipe will explore ways of deploying a [sample application](https://github.com/pivotal-cf/weblogic-k8s-operator-recipe/tree/master/samples/sample-war-proj) to a PKS environment

## Building a Docker Image
The source code for the sample is available with the PKS recipes [here](https://github.com/pivotal-cf/weblogic-k8s-operator-recipe). 

Clone the main project:

```bash
git clone https://github.com/pivotal-cf/weblogic-k8s-operator-recipe
# OR
git clone git@github.com:pivotal-cf/weblogic-k8s-operator-recipe.git
```

Move to the samples folder and edit the `sample-war-proj/gradle.properties` file with a tag with your [dockerhub](https://hub.docker.com/) username:
```yml
dockerTag=my-dockerhub-username/sample-war-proj

# eg. 
# dockerTag=bijukunjummen/sample-war-proj
```

Build the project and create a docker image.  This image builds on the base image `websphere-liberty:webProfile8`. If [microprofile health](https://github.com/eclipse/microprofile-health) is desired, then the base image should be `websphere-liberty:microProfile`.:

```bash
cd samples/sample-war-proj
./gradlew clean createDockerImage
```

A docker image should have been successfully created at this point:
```bash
$ docker images
...
REPOSITORY                              TAG                 IMAGE ID            CREATED             SIZE
bijukunjummen/sample-war-proj           0.0.4-SNAPSHOT      55fbfd30e5ca        2 minutes ago       502MB
...
```

Push this image to a docker repository:
```bash
docker push bijukunjummen/sample-war-proj:0.0.4-SNAPSHOT
```


## Use Websphere Liberty Helm chart to deploy the application

Create a yaml to provide some override values for the [Websphere Liberty Helm Chart](https://github.com/IBM/charts/tree/master/stable/ibm-websphere-liberty/). 

A sample is available in `specs/websphere/sample-war-proj.yml` file:

```yml
image:
  repository: "bijukunjummen/sample-war-proj"
  tag: "0.0.4-SNAPSHOT"

ingress:
  enabled: true
  path: "/sample-war-proj"
  rewriteTarget: "/sample-war-proj"

microprofile:
  health: 
    enabled: false  

autoscaling:
  enabled: true
  minReplicas: 2
  maxReplicas: 10
  targetCPUUtilizationPercentage: 40
   
```

Install the helm chart:

```bash
helm repo add ibm-charts https://raw.githubusercontent.com/IBM/charts/master/repo/stable/
helm install ibm-charts/ibm-websphere-liberty --name liberty-boot-app -f sample-war-proj.yml
```

## Make the service visible to an end user

The service deployed by the helm chart is a `NodePort`, for some reason nodeports are not visible to an enduser and need to be changed to a `LoadBalancer` type. 

```bash
kubectl edit service liberty-boot-app-ibm-web
```

and change the port to 443 and service type to `LoadBalancer`:

```yml
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

```bash
curl -k  -s https://10.195.52.152/sample-war-proj/ping
```

## Upgrading application
Assuming a new docker image with the application packaged in is available, 
change the `specs/websphere/sample-war-proj.yml` with the details of the new image:

```yml
image:
  repository: "bijukunjummen/sample-war-proj"
  tag: "0.0.5-SNAPSHOT"

autoscaling:
  enabled: true
  minReplicas: 3
  maxReplicas: 10
  targetCPUUtilizationPercentage: 40

``` 

and use helm to upgrade the chart:

```
helm upgrade liberty-boot-app ibm-charts/ibm-websphere-liberty -f sample-war-proj.yml
```

And check the status of the deploy:

```bash
helm ls --all liberty-boot-app
```

To see all the Kubernetes resources installed by this chart:

```bash
helm status liberty-boot-app
```

## Delete Application
To completely delete the application, run the following command:

```bash
helm delete --purge liberty-boot-app
```


## References

1. [docker base image](https://github.com/docker-library/docs/tree/master/websphere-liberty) for 
IBM Websphere Liberty
1.  [Websphere Liberty Helm Chart](https://github.com/IBM/charts/tree/master/stable/ibm-websphere-liberty/) 