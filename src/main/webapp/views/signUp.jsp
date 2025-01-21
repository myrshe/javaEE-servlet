<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Регистрация</title>
    <link rel="stylesheet" href="styles/css/signUp.css">
</head>
<body>
<img src="<c:url value="/styles/pictures/logoMini.png"/>" alt="Логотип сайта">
<div class="form-container">
    <h2>Регистрация</h2>
    <form action="/signUp" method="POST">
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required>

        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required>

        <c:if test="${not empty errorMessage}">
            <script type="text/javascript">
                alert("${errorMessage}");
            </script>
        </c:if>
        <p><a href="/signIn" class="auth-link">авторизоваться</a></p>
        <button type="submit">Зарегистрироваться</button>
    </form>
</div>
</body>
</html>
