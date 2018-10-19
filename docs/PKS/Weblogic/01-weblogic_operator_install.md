# Getting Weblogic installed on PKS from scratch using an Operator


## Creating and connecting to your kuberentes cluster.

You'll need a to login to a PKS installation to create a kuberentes cluster. Please make sure you have the correct port for logging in, your login command should look similar to:
 `pks login -a https://api.pks.haas-149.pez.pivotal.io:9021 -k -u USERNAME -p PASSWORD`

Once you've logged in you can create a Kuberentes cluster with:
`pks create-cluster operator-kube-cluster --external-hostname operator-kube-cluster.pks.haas-149.pez.pivotal.io --plan small --num-nodes 3`

You'll need to have give the login/connection information to the kubectl CLI so it can use them in subsequent requests, PKS has a nice command to do this automatically for you: 
`pks get-credentials operator-kube-cluster`

Now you need to set your machine to use the credentials you've just pulled down. To do so, run the command: 
`kubectl config use-context operator-kube-cluster`

## Setup your hosts file so your machine can connect to Kuberenetes.

Often times kubernetes can't setup the DNS entries for the cluster you've created. So the easiest way to get things working is to edit your /etc/hosts file. First we need to find the cluster ip address you can do this with 
`pks cluster operator-kube-cluster` 
This commands output will show both the `Kubernetes Master Host` and the `Kubernetes Master IP(s)`.  You then need create an entry in your /etc/hosts files that does this mapping for you, for requests using the kubectl command to work.  If your output was:

```
Name:                     wls-test
Plan Name:                small
UUID:                     230363f7-62af-46d4-a670-90cc36e0d765
Last Action:              CREATE
Last Action State:        succeeded
Last Action Description:  Instance provisioning completed
Kubernetes Master Host:   wls-test.pks.haas-149.pez.pivotal.io
Kubernetes Master Port:   8443
Worker Instances:         3
Kubernetes Master IP(s):  10.195.2.161
```
you need to add a line that looks like `10.195.2.161    wls-test.pks.haas-149.pez.pivotal.io` do your /etc/hosts file.


## Verify your connection to the Kuberentes Cluster
Now you should be able to verify that you can connect to the Kuberentes cluseter that you've created with the `kubectl cluster-info`  command. You should see an output similar to:

```
Kubernetes master is running at https://operator-kube-cluster.pks.haas-149.pez.pivotal.io:8443
Heapster is running at https://operator-kube-cluster.pks.haas-149.pez.pivotal.io:8443/api/v1/namespaces/kube-system/services/heapster/proxy
KubeDNS is running at https://operator-kube-cluster.pks.haas-149.pez.pivotal.io:8443/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy
monitoring-influxdb is running at https://operator-kube-cluster.pks.haas-149.pez.pivotal.io:8443/api/v1/namespaces/kube-system/services/monitoring-influxdb/proxy

To further debug and diagnose cluster problems, use 'kubectl cluster-info dump'.
```

## Install the Weblogic Operator into our Kuberentes Cluster
Now we are going to install the Oracle Weblogic Operator into our Kuberentes cluster. This will create a custom resource in Kuberentes that manages the weblogic domain (cluster).
Oracle provides instructions at:
[https://blogs.oracle.com/developers/announcing-oracle-weblogic-server-kuberentes-operator](https://blogs.oracle.com/developers/announcing-oracle-weblogic-server-kuberentes-operator)
but in trying to follow them I ran into a number of omissions and problems, The following instructions should be more explicit and complete - but feel free to reference the Oracle documentation for help along the way.

Additionally the above instructions reference Youtube videos for explaining some of the steps, but I found the documentation in the Repository to be more up to date:
[https://github.com/oracle/weblogic-kubernetes-operator/blob/master/site/installation.md](https://github.com/oracle/weblogic-kubernetes-operator/blob/master/site/installation.md)

### Seting up our configuration files to install the operator into our Kuberenetes cluster.

Clone the Weblogic-kubernetes-operator repo:

```
git clone https://github.com/oracle/weblogic-kubernetes-operator/
```

Now we need to modify to prepare the secrets and operator scripts to be able to install the operator into our cluster.

You will need to create a namespace for the operator to be initialized into:
`kubectl create namespace weblogic-operator`

Create a kubernetes secret that contains your dockerhub credentials, so that kuberentes will be able to fetch the image you uploaded:

`kubectl create secret docker-registry my-docker-creds --docker-username=DOCKER_USERNAME --docker-password=SECRET --docker-email=DOCKER_EMAIL --namespace weblogic-operator`


Under the `kubernetes` sub-directory of the `weblogic-kubernetes-operator` project, copy the operator inputs file for customization.

```
 cp create-weblogic-operator-inputs.yaml my-weblogic-operator-inputs.yaml
```

Change the following: 

```yaml
weblogicOperatorImage: oracle/weblogic-kubernetes-operator:1.0
targetNamespaces: weblogic-domain,default
weblogicOperatorImagePullSecretName: my-docker-creds
```

Create a folder for the output of the operator installation command to be captured.
`mkdir create-operator-output/`

### Installing and Verifying the Operator
Then we finally can create the operator in our cluster.
`./create-weblogic-operator.sh -i my-weblogic-operator-inputs.yaml -o create-operator-output/`

You can verify that the operator has created the requisite resources within weblogic with this command
`kubectl -n weblogic-operator get all`

You should see output similar to this:

```
NAME                                     READY     STATUS    RESTARTS   AGE
pod/weblogic-operator-7759668968-6jw86   1/1       Running   0          2m

NAME                                     TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)    AGE
service/internal-weblogic-operator-svc   ClusterIP   10.100.200.174   <none>        8082/TCP   2m
service/kubernetes                       ClusterIP   10.100.200.1     <none>        443/TCP    5h

NAME                                DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/weblogic-operator   1         1         1            1           2m

NAME                                           DESIRED   CURRENT   READY     AGE
replicaset.apps/weblogic-operator-7759668968   1         1         1         2m
```

Logs from the operator are available by querying the pod listed above:
`kubectl -n weblogic-operator logs  weblogic-operator-7759668968-6jw86`

Finally you can see that custom resouce definition has been installed into your kuberentes cluster: 

`kubectl get crd`

```
Outputs:

NAME                      AGE
domains.weblogic.oracle   4m

```

## Next step - deploy the Weblogic domain

You can now proceed to the [next steps](../02-create_weblogic_domain) to deploy the actual Weblogic domain
