package ru.itis.servlets;

import ru.itis.dto.PostDto;
import ru.itis.dto.PostDtoImg;
import ru.itis.models.Image;
import ru.itis.services.ImageService;
import ru.itis.services.PostService;
import ru.itis.services.SubscriptionService;

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


@WebServlet("/feedSubscription")
public class FeedSubscriptionServlet extends HttpServlet {
    private SubscriptionService subscriptionService;
    private PostService postService;
    private ImageService imageService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        subscriptionService = (SubscriptionService) config.getServletContext().getAttribute("subscriptionsService");
        postService = (PostService) config.getServletContext().getAttribute("postService");
        imageService = (ImageService) config.getServletContext().getAttribute("imageService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int user_id = (Integer) session.getAttribute("user_id");

        try {
            List<PostDto> posts = postService.getAllBySubscriptions(user_id);

            List<PostDtoImg> postsImg = new ArrayList<>();

            for (PostDto post : posts) {
                Image image = imageService.getImageByPost(post.getId());
                PostDtoImg postDtoImg = new PostDtoImg(post, image);
                postsImg.add(postDtoImg);
            }


            request.setAttribute("posts", postsImg);

            request.getRequestDispatcher("/views/feedSubscription.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
