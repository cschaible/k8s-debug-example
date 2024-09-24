# K8s-debug
This repo contains a demo application that can be used to showcase interactive K8s debug containers.

## Ingress
Ingress-nginx can be deployed by executing the following helm command.

```bash
helm upgrade --install ingress-nginx ingress-nginx \
--repo https://kubernetes.github.io/ingress-nginx \
--namespace ingress-nginx --create-namespace
```

## Application
The application needs to be built before deploying it to k8s.

### Build application
If necessary adjust the *dockerClient* in the *build.gradle* file. 
```bash
./gradlew clean build jibDockerBuild
```

### Deploy application
```
cd charts
helm upgrade --install k8s-debug k8s-debug
cd -
```

## Port forwarding
```bash
kubectl port-forward --namespace=ingress-nginx service/ingress-nginx-controller 8080:80
```

## Testing the API
```bash
curl http://localhost:8080/data
```

## Enable namespace sharing in the pod to debug
```yaml
spec:
  shareProcessNamespace: true
  containers:
    ...
```

## Run debug pod
```bash
kubectl debug $(kubectl get pods -l app=k8s-debug --no-headers -o custom-columns=":metadata.name") -it --image=cschaible/jcmd-tui:0.1.0 --share-processes -- /bin/bash

# Get process id
$ ps ax

# Run jcmd_tui
$ jcmd_tui --attach <PID>

# Run jcmd directly
$ jattach <PID> jcmd VM.native_memory
```