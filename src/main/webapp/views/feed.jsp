<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Лента новостей</title>
    <link rel="stylesheet" href="<c:url value='/styles/css/header.css'/>">
    <link rel="stylesheet" href="<c:url value='/styles/css/feed.css'/>">
    <link rel="stylesheet" href="<c:url value='/styles/css/post.css'/>">
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

<main class="main">
    <div class="container">
        <c:forEach var="post" items="${posts}">
            <div class="post">
                <div class="post-container">
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
    </div>
</main>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        // Когда весь контент на странице подгрузится сработает этот метод
        const images = document.querySelectorAll(".post-image img[data-src]");
        // document.querySelectorAll выбирает все элементы <img>, которые находятся внутри элементов с классом .post-image и имеют атрибут data-src.
        const lazyLoad = (image) => {
            image.setAttribute("src", image.getAttribute("data-src"));
            image.onload = () => {
                image.removeAttribute("data-src");
            };
        };
        /*Функция lazyLoad принимает элемент изображения (image) в качестве аргумента.
        Она устанавливает атрибут src изображения равным значению атрибута data-src. Это заставляет браузер загрузить изображение.
            После загрузки изображения (image.onload) атрибут data-src удаляется, так как он больше не нужен.
        */
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