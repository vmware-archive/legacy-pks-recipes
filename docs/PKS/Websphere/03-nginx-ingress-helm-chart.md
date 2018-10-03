# Nginx Ingress for Websphere Liberty Helm Chart in PKS

[Websphere Liberty Helm Chart](https://github.com/IBM/charts/tree/master/stable/ibm-websphere-liberty/) creates an ingress assuming the presence of an Nginx based Ingress controller when deploying an application. The ingress is required specifically if Layer 7 functionality like routing to an endpoint based on URI or sticky sessions is needed for application functianality. PKS however comes by default with a NSX based Ingress controller which is not compatible with Webpshere Liberty Helm chart. This recipe will go over how a Nginx based ingress controller can be installed in PKS

## Background

A Pivotal Container Service(PKS) environment by default comes configured with a NSX-T based Ingress Controller, which does not work well with the Websphere Liberty Helm chart. The fix is to install the Nginx based ingress controller also to PKS. Multiple ingress controllers can work at the same time, the ingress controller which configures the ingress depends on the ingress controller which is targeted by the spec, using a `kubernetes.io/ingress.class` annotation key in the Kubernetes Ingress spec:

for eg. to target NSX-T Ingress controller:

```yaml
apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  name: custom-ing-daytrader-nsx
  annotations:
    kubernetes.io/ingress.class: nsx
spec:
  rules:
  - http:
      paths:
      - path: /daytrader.*
        backend:
          serviceName: daytrader-ibm-websphere
          servicePort: 9080
```

or to target Nginx based Ingress controller:

```yaml
apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  annotations:
    kubernetes.io/ingress.class: nginx
    nginx.ingress.kubernetes.io/affinity: cookie
    nginx.ingress.kubernetes.io/rewrite-target: /daytrader
    nginx.ingress.kubernetes.io/session-cookie-hash: sha1
    nginx.ingress.kubernetes.io/session-cookie-name: route
  name: daytrader-ibm-websphere
  namespace: default
spec:
  rules:
  - http:
      paths:
      - backend:
          serviceName: daytrader-ibm-websphere
          servicePort: 9080
        path: /daytrader
```


## Installing an Nginx Ingress controller

Nginx based Ingress controller is available [here](https://github.com/kubernetes/ingress-nginx). It provides a [helm chart](https://github.com/helm/charts) that makes the deployment easy, the details are [here](https://kubernetes.github.io/ingress-nginx/deploy/#using-helm). 

```sh
helm install stable/nginx-ingress --name pks-nginx-ingress
```

Ensure that the controller pods have started up cleanly by monitoring the following command:

```sh
$ kubectl get pods -l  app=nginx-ingress --all-namespaces


NAMESPACE   NAME                                                 READY   STATUS    RESTARTS   AGE
default     pks-nginx-ingress-controller-576f8dff8d-6dq2f        1/1     Running   0          17h
default     pks-nginx-ingress-default-backend-75f98597b7-92kz2   1/1     Running   0          17h
```

Once the controller starts up, the endpoint to access the ingress can be found using the following command:

```sh
$ kubectl get services -l app=nginx-ingress -l component=controller


NAME                           TYPE           CLUSTER-IP      EXTERNAL-IP     PORT(S)                      AGE
pks-nginx-ingress-controller   LoadBalancer   10.100.200.32   10.195.43.136   80:31639/TCP,443:31604/TCP   17h
```