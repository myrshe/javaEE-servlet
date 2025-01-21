<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${profile.username}</title>
    <link rel="stylesheet" href="<c:url value='/styles/css/header.css'/>">
    <link rel="stylesheet" href="<c:url value='/styles/css/post.css'/>">
    <link rel="stylesheet" href="<c:url value='/styles/css/profile.css'/>">
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
                <c:if test="${isAdmin}">
                    <li><a href="/allComments">админка</a></li>
                </c:if>
                <li class="logout"><a href="/logout">выйти</a></li>
            </ul>
        </nav>
    </div>
</header>


<main class="main">
    <div class="container">
        <c:choose>
            <c:when test="${isUserIsSession}">
                <div class="div-name"><h1>Добро пожаловать, ${profile.username}</h1></div>
                <div class="div-email"><h2>Email: ${profile.email}</h2></div>
                <div class="div-createPost">
                    <h4><a href="/createPost">+ Создать пост</a></h4>
                </div>
                <div class="div-posts"><h3>Мои посты:</h3></div>
            </c:when>

            <c:otherwise>
                <div class="div-name"><h1>${profile.username}</h1></div>
                <c:choose>
                    <c:when test="${isFollow}">
                        <form action="/profile/${profile.id}/unsubscribe" method="post">
                            <button class="button-sub-unsub" type="submit">Отписаться</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <form action="/profile/${profile.id}/subscribe" method="post">
                            <button class="button-sub-unsub" type="submit">Подписаться</button>
                        </form>
                    </c:otherwise>
                </c:choose>
                <div class="div-posts"><h3>посты ${profile.username}:</h3></div>
            </c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${not empty posts}">
                <c:forEach var="post" items="${posts}">
                    <div class="post">
                        <div class="post-container">
                            <a href="/profile/${post.postDto.user_id}"><h2>${post.postDto.authorUsername}</h2></a>
                            <c:if test="${post.postDto != null}">
                                <p>${post.postDto.text}</p>
                                <c:if test="${null != post.image }">
                                    <figure class="post-image">
                                        <img data-src="${pageContext.request.contextPath}/uploaded/files?id=${post.image.id}"
                                             alt="image"
                                             loading="lazy">
                                    </figure>
                                </c:if>
                                <small>${post.postDto.date}</small>
                            </c:if>
                        </div>
                        <div class="utility-container">
                            <img id="like-button-${post.postDto.id}"
                                 src="/styles/pictures/empty-heart.png"
                                 alt="Лайк"
                                 class="like-button like-icon"
                                 data-post-id="${post.postDto.id}">

                            <a href="/post?id=${post.postDto.id}">
                                <img src="/styles/pictures/comment.png" alt="Комментарии">
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p>Пока постов нет</p>
            </c:otherwise>
        </c:choose>
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
<script>
    async function toggleLike(button) {
        const postId = button.getAttribute('data-post-id');
        const likeIcon = button; // Используем саму кнопку

        try {
            const response = await fetch(`/toggle-like`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ postId }),
            });

            if (!response.ok) {
                throw new Error('Ошибка при обработке лайка');
            }

            const result = await response.json();

            // Меняем изображение в зависимости от состояния лайка
            if (result.liked) {
                likeIcon.src = '/styles/pictures/filled-heart.png'; // Заполненное сердце
            } else {
                likeIcon.src = '/styles/pictures/empty-heart.png'; // Пустое сердце
            }
        } catch (error) {
            console.error('Ошибка:', error);
        }
    }

    document.addEventListener('DOMContentLoaded', function () {
        const likeButtons = document.querySelectorAll('.like-button');
        likeButtons.forEach(button => {
            button.addEventListener('click', function () {
                toggleLike(this);
            });
        });
    });
</script>

</body>
</html>
