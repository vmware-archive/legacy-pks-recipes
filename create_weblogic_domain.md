Creating a Weblogic Domain using the Operator.

## Prerequisite
A prerequisite of creating a Weblogic domain using these steps is to first have the Operator installed into your Kuberentes cluster - There are instructions for doing so in the same folder.
An additional prerequisite for this process is to be able to create a Persistent Volume (https://kubernetes.io/docs/concepts/storage/persistent-volumes/) In your cluster that supports the Access mode of ReadWriteMany.
I would caution against trying to use VsphereVolume(s) as of 2018/8/2 it does not appear to work with the access mode - something that is ambiguous

## Verifying the Persistent Volume
In order to verify that we're able to create and use Persistent Volumes I would suggest that we build some sample containers that utilize the persistent volumes and verify their access.

In this directory are a few files that you will use to create these persistent volumes and containers. pv-test.yaml will create the Persistent Volume, pvc-test.yaml will create the Persistent Volume Claim and nginx.yaml will create an Nginx reverse proxy (Just an arbitrary sample container) that attaches to the persistent volume.

`kubectl create -f pv-test.yaml`
`kubectl create -f pvc-test.yaml`
`kubectl create -f nginx.yaml`

After completing these steps you should verify that your claims and pods have completed creation successfully.

`kubectl get pvc`

```
NAME      STATUS    VOLUME    CAPACITY   ACCESS MODES   STORAGECLASS   AGE
nfs       Bound     nfs       10Gi       RWX                           11s
 ```

`kubectl get pods`

```
NAME                                 READY     STATUS              RESTARTS   AGE
nginx                                0/1       ContainerCreating   0          8m
weblogic-operator-7759668968-6jw86   1/1       Running             0          22h
```

At this point I'd like to add another tool into your arsenal for debugging what's going on your cluster. If your not already aware you can use the

`kubectl proxy` on your local machine to open up a connection to the kuberenetes web interface. The web interface is then available at `http://localhost:8001/api/v1/namespaces/kube-system/services/https:kubernetes-dashboard:/proxy/#!/overview`


Now we can log into the pod we just created and make sure that there an appropriately attached storage entry.
