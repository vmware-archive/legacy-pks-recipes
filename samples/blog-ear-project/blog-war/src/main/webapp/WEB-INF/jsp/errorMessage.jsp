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
        <h1 class="display-4">Error:  <c:out value="${message}"/></h1>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/bootstrap.js"></script>

</body>
</html>