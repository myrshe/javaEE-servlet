package ru.itis.servlets;

import ru.itis.dto.CommentDto;
import ru.itis.services.CommentService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/allComments")
public class AllCommentsServlet extends HttpServlet {

    private CommentService commentService; // Сервис для работы с комментариями

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        commentService = (CommentService) config.getServletContext().getAttribute("commentService"); // Инициализация сервиса
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<CommentDto> comments = commentService.allComments();
            request.setAttribute("comments", comments);

            request.getRequestDispatcher("views/allComments.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commentId = request.getParameter("commentId");

        if (commentId != null && !commentId.trim().isEmpty()) {
            try {
                // Удаляем комментарий
                commentService.removeComment(Integer.parseInt(commentId));
                response.sendRedirect(request.getContextPath() + "/allComments"); // Перенаправляем обратно на страницу со списком комментариев
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid comment ID");
            } catch (SQLException e) {
                throw new ServletException("Error deleting comment", e);
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Comment ID is missing");
        }
    }
}
