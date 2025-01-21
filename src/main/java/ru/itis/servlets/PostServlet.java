package ru.itis.servlets;

import ru.itis.dto.CommentDto;
import ru.itis.dto.PostDto;
import ru.itis.dto.PostDtoImg;
import ru.itis.models.Comment;
import ru.itis.models.Image;
import ru.itis.models.Post;
import ru.itis.models.User;
import ru.itis.repositories.repo.CommentRepository;
import ru.itis.repositories.repo.PostRepository;
import ru.itis.repositories.repo.UserRepository;
import ru.itis.services.CommentService;
import ru.itis.services.ImageService;
import ru.itis.services.PostService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/post")
public class PostServlet extends HttpServlet {

    private CommentService commentService;
    private PostService postService;
    private ImageService imageService;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        commentService = (CommentService) config.getServletContext().getAttribute("commentService");
        postService = (PostService) config.getServletContext().getAttribute("postService");
        imageService = (ImageService) config.getServletContext().getAttribute("imageService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int post_id = Integer.parseInt(request.getParameter("id"));

        try {
            PostDto postDto = postService.findById(post_id);
            Image image = imageService.getImageByPost(postDto.getId());
            PostDtoImg post = new PostDtoImg(postDto, image);
            List<CommentDto> comments = commentService.findByPostId(post_id);
            request.setAttribute("post", post);
            request.setAttribute("comments", comments);
            request.getRequestDispatcher("/views/post.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer user_id = (Integer) session.getAttribute("user_id");

        int post_id = Integer.parseInt(request.getParameter("post_id"));
        String text = request.getParameter("text");

        String authorUsername = (String) session.getAttribute("username");

        CommentDto comment = new CommentDto();
        comment.setText(text);
        comment.setPost_id(post_id);
        comment.setUser_id(user_id);
        comment.setDate(LocalDateTime.now());
        comment.setAuthorUsername(authorUsername);

        try {
            commentService.createComment(comment);
            response.sendRedirect("/post?id=" + post_id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
