package ru.itis.servlets;

import ru.itis.dto.PostDto;
import ru.itis.dto.PostDtoImg;
import ru.itis.dto.ProfileDto;
import ru.itis.dto.SubscriptionDto;
import ru.itis.models.Image;
import ru.itis.services.*;

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

@WebServlet("/profile/*")
public class ProfileServlet extends HttpServlet {

    private ProfileService profileService;
    private ImageService imageService;
    private PostService postService;
    private SubscriptionService subscriptionService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        profileService = (ProfileService) config.getServletContext().getAttribute("profileService");
        imageService = (ImageService) config.getServletContext().getAttribute("imageService");
        postService = (PostService) config.getServletContext().getAttribute("postService");
        subscriptionService = (SubscriptionService) config.getServletContext().getAttribute("subscriptionService");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer user_id = (Integer) session.getAttribute("user_id");
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        int id = Integer.parseInt(pathParts[1]);
        try {
            ProfileDto profileDto = profileService.getProfileById(id);


            boolean isUserIsSession = user_id == id;

            if (!isUserIsSession) {
                boolean isSubscribedPage = pathParts.length > 2 && "subscribed".equals(pathParts[2]);
                boolean isFollow = subscriptionService.isSubscribed(user_id, id);
                request.setAttribute("isFollow", isFollow);

                if (isFollow && !isSubscribedPage) {
                    response.sendRedirect("/profile/" + id + "/subscribed");
                    return;
                }
                if (!isFollow && isSubscribedPage) {
                    response.sendRedirect("/profile/" + id);
                    return;
                }

            }

            String  role  = (String) session.getAttribute("role");
            boolean isAdmin = "admin".equals(role);
            request.setAttribute("isAdmin", isAdmin);

            List<PostDto> posts = postService.getAllByUser(profileDto.getId(), user_id);
            List<PostDtoImg> postsImg = new ArrayList<>();

            for (PostDto post : posts) {
                Image image = imageService.getImageByPost(post.getId());
                PostDtoImg postDtoImg = new PostDtoImg(post, image);
                postsImg.add(postDtoImg);
            }


            request.setAttribute("isUserIsSession", isUserIsSession);
            request.setAttribute("profile", profileDto);
            request.setAttribute("posts", postsImg);
            request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Integer user_id = (Integer) session.getAttribute("user_id");
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");

        if (pathParts.length < 3) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL");
            return;
        }

        int following_id = Integer.parseInt(pathParts[1]);
        String action = pathParts[2];

        try {
            SubscriptionDto subscriptionDto = new SubscriptionDto();
            subscriptionDto.setUser_id(user_id);
            subscriptionDto.setFollowing_id(following_id);

            if ("unsubscribe".equals(action)) {
                subscriptionService.unSubscribe(subscriptionDto);
                response.sendRedirect("/profile/" + following_id); // После отписки перенаправляем на обычный URL
            } else if ("subscribe".equals(action)) {
                subscriptionService.subscribe(subscriptionDto);
                response.sendRedirect("/profile/" + following_id + "/subscribed"); // После подписки перенаправляем на /subscribed
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
