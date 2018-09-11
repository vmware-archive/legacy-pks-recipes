This is a JPA based JEE project representing a simple Blog domain. There are three entities that are managed in this application:

1. Users
1. Post
1. Comments

The following JEE Technologies are used:
1. JAX-RS for REST API
2. EJB Entity beans/JPA
3. JSP for UI
4. CDI for dependency management
5. Servlet API


## Users

```
BASE_URL=???
# For Minikube
export NODE_IP=$(minikube ip)
export NODE_PORT=$(kubectl get --namespace default -o jsonpath="{.spec.ports[0].nodePort}" services blog-ear-app-ibm-websphe)
BASE_URL=https://$NODE_IP:$NODE_PORT

# Create Users
http --verify=no PUT "${BASE_URL}/blog-root/api/user/create" fullname=user1 password=user1 email=user1@test.com -v
http --verify=no PUT "${BASE_URL}/blog-root/api/user/create" fullname=user2 password=user2 email=user2@test.com -v

# Get Users
http --verify=no GET "${BASE_URL}/blog-root/api/user/list"

```


## Posts
```
# Create Post
http --verify=no PUT "${BASE_URL}/blog-root/api/post/create?userId=1" title="My First Post" content="My Post Content"

# Get Posts
http --verify=no GET "${BASE_URL}/blog-root/api/post/list" -v
```

