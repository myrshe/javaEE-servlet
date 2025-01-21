<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Все комментарии</title>
    <link rel="stylesheet" href="<c:url value='/styles/css/header.css'/>">
    <link rel="stylesheet" href="<c:url value='/styles/css/comments.css'/>">
    <link rel="stylesheet" href="<c:url value='/styles/css/global.css'/>">
</head>
<body>

<header class="header">
    <div class="header">
        <nav class="nav-header">
            <ul>
                <li>
                    <a class="top" href="">
                        <img src="<c:url value="/styles/pictures/logoMini.png"/>" alt="Логотип сайта">
                    </a>
                </li>
                <li><a href="/profile/${sessionScope.user_id}">мой профиль</a></li>
                <li><a href="/feed">главная лента</a></li>
                <li><a href="/feedSubscription">лента подписок</a></li>
            </ul>
        </nav>
    </div>
</header>
<main>
    <div class="content">
        <p>комментарии:</p>
        <c:forEach var="comment" items="${comments}">
            <div class="comment">
                <a href="/profile/${comment.user_id}"><p>${comment.authorUsername}</p></a>
                <p>${comment.text}</p>
            </div>
            <form action="${pageContext.request.contextPath}/allComments" method="post">
                <input type="hidden" name="commentId" value="${comment.id}">
                <button class="button-delete" type="submit">delete</button>
            </form>
        </c:forEach>
    </div>
</main>
</body>
</html>

