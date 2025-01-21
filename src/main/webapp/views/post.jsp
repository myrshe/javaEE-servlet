<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Публикация</title>
    <link rel="stylesheet" href="<c:url value='/styles/css/header.css'/>">
    <link rel="stylesheet" href="<c:url value='/styles/css/postAndComments.css'/>">
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
        <div class="post">
            <a href="/profile/${post.postDto.user_id}"><h2>${post.postDto.authorUsername}</h2></a>
            <c:if test="${post.postDto != null}">
                <p>${post.postDto.text}</p>
                <c:if test="${null != post.image}">
                    <figure class="post-image">
                        <img data-src="${pageContext.request.contextPath}/uploaded/files?id=${post.image.id}"
                             alt="image"
                             loading="lazy">
                    </figure>
                </c:if>
                <small>${post.postDto.date}</small>
            </c:if>
        </div>

        <h3>Добавить комментарий:</h3>
        <form action="/post" method="post">
            <input type="hidden" name="post_id" value="${post.postDto.id}">
            <textarea name="text" rows="10" cols="200" required></textarea><br>
            <button type="submit">Отправить</button>
        </form>

        <p>комментарии:</p>
        <c:forEach var="comment" items="${comments}">
            <div class="comment">
                <a href="/profile/${comment.user_id}"><p>${comment.authorUsername}</p></a>
                <p>${comment.text}</p>
            </div>
        </c:forEach>
    </div>
</main>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const images = document.querySelectorAll(".post-image img[data-src]");

        const lazyLoad = (image) => {
            image.setAttribute("src", image.getAttribute("data-src"));
            image.onload = () => {
                image.removeAttribute("data-src");
            };
        };

        const observer = new IntersectionObserver((entries, observer) => {
            entries.forEach((entry) => {
                if (entry.isIntersecting) {
                    lazyLoad(entry.target);
                    observer.unobserve(entry.target);
                }
            });
        });

        images.forEach((img) => {
            observer.observe(img);
        });
    });
</script>

</body>
</html>