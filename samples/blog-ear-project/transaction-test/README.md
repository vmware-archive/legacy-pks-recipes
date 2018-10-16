## Testing Recovery of Distributed Transactions

If Distributed transactions are enabled, then the Websphere helm chart uses a Kubernetes Stateful set
to manage pods, and each pod is assigned a Persistent volume claim that is restored if a pod dies.

The main reason to do this is to ensure that any distributed transaction on the fly can be recovered
if a pod is killed for any reason.

The way to test is fairly involved ( see [this](https://developer.ibm.com/wasdev/docs/managing-global-xa-transactions-aws-websphere-liberty/)) , the rough flow is the following:

1. Include a distributed transaction interceptor with the docker image holding the application, 
the purpose of the interceptor will be to simply kill the pod once a distributed transaction 
is about to commit. The interceptor is activated using a flag

1. Turn the flag off and do a helm upgrade

1. Kill the pod manually so that the helm upgraded version of the app starts up

1. At this point the transaction should be picked back up and executed.


### Interceptor codebase

The interceptor code base is avaiable here - https://github.com/WASdev/sample.aws/tree/master/transactionRecovery

The callback part of the codebase has been ported over to the [samples](https://github.com/pivotal-cf/legacy-pks-recipes/tree/master/samples) folder of this repo.

The jar built from the codebase is available in this folder

### Creating an image

Create the docker image of the application with the interceptor jar baked in:

```
docker build -t bijukunjummen/blog-ear-project:0.0.15-TRANSACTION .
```

and push this image to docker hub.

### Using the image for bringing up the application

The helm chart values for the application looks like this:

```
image:
  repository: "bijukunjummen/blog-ear-project"
  tag: "0.0.15-TRANSACTION"

ingress:
  enabled: true
  path: "/blog-root"
  rewriteTarget: "/blog-root"


logs:
  persistTransactionLogs: true

replicaCount: 1

env:
  jvmArgs: "-Dcom.ibm.ws.Transaction.fvt=true -Dapp.servername=wv-blog-db-mysql -Dapp.user=bloguser -Dapp.password=blogpassword -Dapp.database=blogdb -Dapp.servername2=wv-blog-db-2-mysql -Dapp.user2=bloguser -Dapp.password2=blogpassword -Dapp.database2=blogdb"

ssl:
  enabled: false

service:
  type: ClusterIP
  port: 9080
  targetPort: 9080


persistence:
  storageClassName: vsphere-storage

```



Adding this flag `-Dcom.ibm.ws.Transaction.fvt=true` turns on the interceptor functionality.

Use these values to start up the application:

```
helm install ibm-charts/ibm-websphere-liberty --name blog-ear-app -f blog-ear-project.yml
```

An endpoint which adds values to two databases is available here:

```
http://BASE-URL/blog-root/api/transaction/success
```

The pod should be in a killed state as soon as this call is made, just upgrade the helm chart with the following values:



```
image:
  repository: "bijukunjummen/blog-ear-project"
  tag: "0.0.15-TRANSACTION"

ingress:
  enabled: true
  path: "/blog-root"
  rewriteTarget: "/blog-root"


logs:
  persistTransactionLogs: true

replicaCount: 1

env:
  jvmArgs: "-Dapp.servername=wv-blog-db-mysql -Dapp.user=bloguser -Dapp.password=blogpassword -Dapp.database=blogdb -Dapp.servername2=wv-blog-db-2-mysql -Dapp.user2=bloguser -Dapp.password2=blogpassword -Dapp.database2=blogdb"

ssl:
  enabled: false

service:
  type: ClusterIP
  port: 9080
  targetPort: 9080


persistence:
  storageClassName: vsphere-storage

```

The  `-Dcom.ibm.ws.Transaction.fvt=true` flag has been removed now.

Once upgrade is complete, killing the pod should bring up a new pod without the callback and the transaction should
complete cleanly.

