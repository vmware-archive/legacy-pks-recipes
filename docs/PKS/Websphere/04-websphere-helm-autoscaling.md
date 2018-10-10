# Autoscaling Websphere apps

[Websphere Liberty Helm Chart](https://github.com/IBM/charts/tree/master/stable/ibm-websphere-liberty/) can autoscale applications based on CPU utilization. It internally uses a [Horizontal POD autoscaler](https://kubernetes.io/docs/tasks/run-application/horizontal-pod-autoscale/) Kubernetes resource to enable scaling functionality.

## Enabling Autoscaling

Autoscaling can be enabled purely using helm values. Consider the following values being used to spin up resources using the Websphere Liberty Helm Chart:

```yaml
image:
  repository: "mydockerorg/sample-war-proj"
  tag: "0.0.8-SNAPSHOT"

ingress:
  enabled: true
  path: "/sample-war-proj"
  rewriteTarget: "/sample-war-proj"

autoscaling:
  enabled: true
  minReplicas: 2
  maxReplicas: 10
  targetCPUUtilizationPercentage: 40

resources:
  constraints:
    enabled: true
  requests:
    cpu: 200m

replicaCount: 2

logs:
  persistTransactionLogs: false

ssl:
  enabled: false

service:
  type: ClusterIP
  port: 9080
  targetPort: 9080

  
```

Here `autoscaling.enabled` property is enabled, and the autoscaling is being set to take effect when the CPU utilization goes above 40%. 

The created HPA kubernetes resource would initially scale the pods based on provided `autoscaling.minReplicas` and will scale up to `autoscaling.maxReplicas` based on usage. It also ensures that the pods are scaled down if the load is consistently below the threshold.

