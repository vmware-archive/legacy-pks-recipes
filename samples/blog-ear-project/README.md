This is a JPA based JEE project representing a simple Blog domain. There are three entities that are managed in this application:

1. Users
1. Post
1. Comments

## Users

```
# Create Users
http PUT 'http://baseurl/blog-root/resources/api/user/create' fullname=user1 password=user1 email=user1@test.com -v
http PUT 'http://baseurl/blog-root/resources/api/user/create' fullname=user2 password=user2 email=user2@test.com -v

# Get Users
http GET 'http://baseurl/blog-root/resources/api/user/list'

```


## Posts
```
# Create Post
http PUT 'http://baseurl/blog-root/resources/api/post/create?userId=1' title="My First Post" content="My Post Content"  -v

# Get Posts
http GET 'http://baseurl/blog-root/resources/api/post/list' -v
```