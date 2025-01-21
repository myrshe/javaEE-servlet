<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Создайте новый пост!</title>
    <link rel="stylesheet" href="<c:url value='/styles/css/header.css'/>">
    <link rel="stylesheet" href="<c:url value='/styles/css/createPost.css'/>">
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
        <div class="form-container post">
            <h2>Create a New Post</h2>

            <%-- Отображение сообщения об ошибке --%>
            <c:if test="${not empty errorMessage}">
                <div class="error-message">${errorMessage}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/createPost" method="post" enctype="multipart/form-data">
                <label for="text">Post Text:</label>
                <textarea id="text" name="text" placeholder="Write your post here..." required></textarea>

                <label for="file">Upload File (optional):</label>
                <input type="file" id="file" name="file" accept="image/*">

                <input type="hidden" name="user_id" value="${sessionScope.userId}">

                <input type="submit" value="Create Post">
            </form>
        </div>
    </div>
</main>
</body>
</html>
