package ru.itis.servlets;

import ru.itis.dto.PostDto;
import ru.itis.dto.PostDtoImg;
import ru.itis.models.Image;
import ru.itis.models.Post;
import ru.itis.repositories.repo.PostRepository;
import ru.itis.repositories.repo.UserRepository;
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
import java.util.ArrayList;
import java.util.List;


@WebServlet("/feed")
public class FeedServlet extends HttpServlet {
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
        HttpSession session = request.getSession();
        Integer user_id = (Integer) session.getAttribute("user_id");
        try {
            List<PostDto> posts = postService.getAll(user_id);
            List<PostDtoImg> postsImg = new ArrayList<>();

            for (PostDto post : posts) {
                Image image = imageService.getImageByPost(post.getId());
                PostDtoImg postDtoImg = new PostDtoImg(post, image);
                postsImg.add(postDtoImg);
            }

            request.setAttribute("posts", postsImg);

            request.getRequestDispatcher("/views/feed.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

}
