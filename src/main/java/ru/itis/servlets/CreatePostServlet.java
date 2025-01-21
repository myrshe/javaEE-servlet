package ru.itis.servlets;

import ru.itis.dto.PostDto;
import ru.itis.services.ImageService;
import ru.itis.services.PostService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

@WebServlet("/createPost")
@MultipartConfig
public class CreatePostServlet extends HttpServlet {
    private PostService postService;
    private ImageService imageService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        postService = (PostService) config.getServletContext().getAttribute("postService");
        imageService = (ImageService) config.getServletContext().getAttribute("imageService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("views/createPost.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String text = request.getParameter("text");


        Part filePart = request.getPart("file");

        HttpSession session = request.getSession();
        String authorUsername = (String) session.getAttribute("username");
        int user_id = (Integer) session.getAttribute("user_id");

        try {
            if (text.isEmpty() || text.trim().isEmpty()) {
                request.setAttribute("errorMessage", "You haven't entered anything.");;
            }

            PostDto postDto = PostDto.builder()
                    .text(text)
                    .user_id(user_id)
                    .authorUsername(authorUsername)
                    .date(LocalDateTime.now())
                    .build();
            int  post_id = postService.createPostAndReturnId(postDto);

            if (filePart != null && filePart.getSize() > 5) {
                imageService.saveFileToStorage(
                        filePart.getInputStream(),
                        filePart.getSubmittedFileName(),
                        filePart.getContentType(),
                        post_id,
                        filePart.getSize()
                );
            }
            response.sendRedirect("/profile/" + user_id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        /*String date = req.getParameter("date");
        String text = req.getParameter("text");
        Long userId = Long.parseLong(req.getParameter("userId"));

        Part part = req.getPart("file");


        try {
            dayService.createDay(DayDto.builder()
                    .text(text)
                    .date(date)
                    .userId(userId)
                    .build());
            DayDto dayDto = dayService.getDayDtoLast(userId);
            if (part.getSize() > 5) {
                imageService.saveFileToStorage(part.getInputStream(), part.getSubmittedFileName(), part.getContentType(), dayDto.getId(), part.getSize());
            }

            resp.sendRedirect(req.getContextPath() + "/profile/" + req.getSession().getAttribute("username"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
    }
}
