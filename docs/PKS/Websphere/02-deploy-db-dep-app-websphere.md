# Deploying a JEE app with a Database dependency using Websphere Liberty Helm Chart

The sample being deployed here is a JEE application for a blog site. It has 3 entities:

1. User 
2. Post 
3. Comment 

The entities are persisted into a database. The datasource is expected to be provided using a JNDI resource. 

The application provides rest endpoints to these entities. Additionally a JSP based UI allows a user to login and add/edit/delete posts.

## Installing the Database

Create the persistent volume for the database with an accessmode of `ReadWriteOnce`. Modify the `mysql-pv-sample.yml` file for the right values in the environment and create the persistent volume

```
cd spec/data-services
kubectl apply -f mysql-pv-sample.yml
```

[Helm Charts](https://helm.sh/) will be used for deploying the database.

`Helm` is a client side utility and `tiller` is the server side counterpart orchestrating a deploy

Follow the instructions [here](https://docs.helm.sh/using_helm) to install helm chart. PKS requires a service account for tiller, the backend component of Helm chart, for right permissions to access Kubernetes API, the instructions to setup this account is [here](https://docs.pivotal.io/runtimes/pks/1-1/configure-tiller-helm.html)


Once tiller is in place, run the following command to deploy mysql:

```
helm install --name blog-db -f mysql-values.yml stable/mysql
```

## Test the database

A sample pod with ubuntu installed in it can be deployed the following way and used for testing the mysql database:

```
cd data-services
kubectl apply -f ubuntu-debug.yml
kubectl exec -i --tty ubuntu-debug /bin/bash

# Using the resulting bash prompt
apt update && apt install -y mysql-client
mysql -h blog-db-mysql -u bloguser -D blogdb --password=blogpassword

# At mysql prompt
show tables;
```

## Building a Docker Image for the Application
The source code for the sample is available with the PKS recipes [here](https://github.com/pivotal-cf/weblogic-k8s-operator-recipe). 

Clone the main project:

```bash
git clone https://github.com/pivotal-cf/weblogic-k8s-operator-recipe
# OR
git clone git@github.com:pivotal-cf/weblogic-k8s-operator-recipe.git
```

The sample is in `samples/blog-ear-project` folder

Build the project:

```
cd samples/blog-ear-project
./mvnw clean package
./build-liberty-docker-image.sh
```

This builds a `bijukunjummen/blog-ear-project:0.0.2-SNAPSHOT` file. This image is already published in dockerhub so if needed this step can be bypassed.


## Use Websphere Liberty Helm chart to deploy the application

Create a yaml to provide some override values for the [Websphere Liberty Helm Chart](https://github.com/IBM/charts/tree/master/stable/ibm-websphere-liberty/). 

A sample is available in `specs/websphere/blog-ear-project.yml` file:

```yml
image:
  repository: "bijukunjummen/blog-ear-project"
  tag: "0.0.2-SNAPSHOT"

ingress:
  enabled: true
  path: "/blog-root"
  rewriteTarget: "/blog-root"

microprofile:
  health: 
    enabled: false  

autoscaling:
  enabled: true
  minReplicas: 2
  maxReplicas: 10
  targetCPUUtilizationPercentage: 40
  
env:
  jvmArgs: "-Dapp.servername=blog-db-mysql -Dapp.user=bloguser -Dapp.password=blogpassword -Dapp.database=blogdb"  
```

The credentials to the database are being provided as JVM arguments.


Install the helm chart:

```bash
helm repo add ibm-charts https://raw.githubusercontent.com/IBM/charts/master/repo/stable/
cd specs/websphere
helm install ibm-charts/ibm-websphere-liberty --name blog-ear-app -f blog-ear-project.yml
```

## Make the service visible to an end user

The service deployed by the helm chart is a `NodePort`, for some reason nodeports are not visible to an enduser and need to be changed to a `LoadBalancer` type. 

```bash
kubectl edit service blog-ear-app-ibm-websphe
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
    app: blog-ear-app-ibm-websphe
  sessionAffinity: None
  type: LoadBalancer
```

Grab the ip for the loadbalancer:

```
kubectl get service blog-ear-app-ibm-websphe -o 'jsonpath={.status.loadBalancer.ingress[0].ip}'
```

## Test the app
If the deployment has completed, it can be tested using a curl command against the load balancer ip:

```bash
curl -k  -s https://10.195.52.152/blog-root/
```

## Delete Application
To completely delete the application, run the following command:

```bash
helm delete --purge blog-ear-app
```
## References

1. [docker base image](https://github.com/docker-library/docs/tree/master/websphere-liberty) for 
IBM Websphere Liberty
1.  [Websphere Liberty Helm Chart](https://github.com/IBM/charts/tree/master/stable/ibm-websphere-liberty/) 