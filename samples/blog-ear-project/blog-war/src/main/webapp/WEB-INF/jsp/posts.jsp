<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Posts By User</title>
    <!-- Bootstrap core CSS -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet">
    <link rel="stylesheet" href='https://fonts.googleapis.com/css?family=Source+Sans+Pro'/>
    <style>
        * {
            font-family: 'Source Sans Pro', sans-serif;
        }
    </style>
    
</head>

<body>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/web/closed/showPosts">JEE Based Blog</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="${pageContext.request.contextPath}/web/closed/showPosts">Show Posts of Active
                    User: <c:out value="${activeUser.fullname}"/></a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/web/open/changeActiveUser">Change Active
                    User</a>
            </li>
        </ul>
    </div>
</nav>

<div class="container-fluid">
    <div class="jumbotron">
        <h1 class="display-4">Posts by <c:out value="${activeUser.fullname}"/></h1>
    </div>
    <c:forEach items="${posts}" var="post">
        <div class="row">
            <div class="col-sm-1"></div>
            <div class="col-sm-10">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title"><c:out value="${post.title}"/></h5>
                        <h6 class="card-subtitle mb-2 text-muted">By <c:out value="${post.user.fullname}"/> on
                            <fmt:formatDate type="both" dateStyle="medium" timeStyle="medium"
                                            value="${post.created}"/></h6>
                        <p class="card-text"><c:out value="${post.content}"/></p>
                        <a href="${pageContext.request.contextPath}/web/closed/editPostForm?id=${post.id}" class="card-link">Edit</a>
                        <a href="${pageContext.request.contextPath}/web/closed/deletePost?id=${post.id}" class="card-link">Delete</a>
                    </div>
                </div>
                <hr/>
            </div>
            <div class="col-sm-1"></div>
        </div>
    </c:forEach>

    <div class="row">
        <div class="col-sm-1"></div>
        <div class="col-sm-10">
            <form method="post" action="${pageContext.request.contextPath}/web/closed/savePost">
                <div class="form-group">
                    <label for="title">Title</label>
                    <input type="text" class="form-control" id="title" name="title"/>
                </div>
                <div class="form-group">
                    <label for="content">Content</label>
                    <textarea type="text" class="form-control" id="content" name="content" rows="10"></textarea>
                </div>
                <button type="submit" class="btn btn-primary">Create Post</button>
            </form>

        </div>
        <div class="col-sm-1"></div>
    </div>

</div>

<script src="${pageContext.request.contextPath}/js/bootstrap.js"></script>

</body>
</html>