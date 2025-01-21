package ru.itis.servlets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import ru.itis.repositories.repo.LikeRepository;
import ru.itis.services.LikeService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/toggle-like")
public class ToggleLikeServlet extends HttpServlet {
    private LikeService likeService;
    private LikeRepository likeRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        likeService = (LikeService) config.getServletContext().getAttribute("likeService");
        likeRepository = (LikeRepository) config.getServletContext().getAttribute("likeRep");
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer user_id = (Integer) session.getAttribute("user_id");
        // Чтение данных из запроса
        BufferedReader reader = request.getReader();
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        String json = jsonBuilder.toString();

        // Парсинг JSON
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        int postId = jsonObject.get("postId").getAsInt();

        // Вызов сервиса для обработки лайка
        boolean liked = likeService.toggleLike(user_id, postId);

        // Формирование JSON-ответа
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("liked", liked);

        // Отправка ответа
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseJson.toString());
    }
}