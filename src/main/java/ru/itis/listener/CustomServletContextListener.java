package ru.itis.listener;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.itis.repositories.repo.*;
import ru.itis.services.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class CustomServletContextListener implements ServletContextListener {
    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "123";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/sem_work";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DB_DRIVER);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);
        dataSource.setUrl(DB_URL);

        UserRepository userRepository = new UserRepositoryImpl(dataSource);
        PostRepository postRepository = new PostRepositoryImpl(dataSource);
        CommentRepository commentRepository = new CommentRepositoryImpl(dataSource);
        ImageRepository imageRepository = new ImageRepositoryImpl(dataSource);
        SubscriptionRepository subscriptionRepository = new SubscriptionRepositoryImpl(dataSource);
        LikeRepository likeRepository = new LikeRepositoryImpl(dataSource);


        SubscriptionService subscriptionService = new SubscriptionServiceImpl(userRepository, subscriptionRepository);
        servletContext.setAttribute("subscriptionService", subscriptionService);

        SignInService signInService = new SignInServiceImpl(userRepository);
        servletContext.setAttribute("signInService", signInService);

        SignUpService signUpService = new SignUpServiceImpl(userRepository);
        servletContext.setAttribute("signUpService", signUpService);

        ProfileService profileService = new ProfileServiceImpl(userRepository, postRepository);
        servletContext.setAttribute("profileService", profileService);

        PostService postService = new PostServiceImpl(postRepository, userRepository, subscriptionRepository, likeRepository);
        servletContext.setAttribute("postService", postService);

        ImageService imageService = new ImageServiceImpl(imageRepository);
        servletContext.setAttribute("imageService", imageService);

        CommentService commentService = new CommentServiceImpl(userRepository, commentRepository);
        servletContext.setAttribute("commentService", commentService);

        LikeService likeService = new LikeServiceImpl(likeRepository);
        servletContext.setAttribute("likeService", likeService);
        servletContext.setAttribute("likeRep", likeRepository);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}