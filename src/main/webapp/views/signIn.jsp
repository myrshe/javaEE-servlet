<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Авторизация</title>
    <link rel="stylesheet" href="styles/css/signIn.css">

</head>
<body>
<img src="<c:url value="/styles/pictures/logoMini.png"/>" alt="Логотип сайта">
<div class="form-container">
    <h1>virtuoz</h1>
    <span>вход</span>
    <form action="/signIn" method="POST">

        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required>
        <c:if test="${not empty errorMessage}">
            <script type="text/javascript">
                alert("${errorMessage}");
            </script>
        </c:if>
        <p><a href="/signUp" class="auth-link">зарегистрироваться</a></p>
        <button type="submit">войти</button>

    </form>
</div>
</body>
</html>
