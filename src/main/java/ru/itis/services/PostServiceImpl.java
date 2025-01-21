package ru.itis.services;

import ru.itis.dto.PostDto;
import ru.itis.models.Post;
import ru.itis.models.Subscription;
import ru.itis.repositories.repo.LikeRepository;
import ru.itis.repositories.repo.PostRepository;
import ru.itis.repositories.repo.SubscriptionRepository;
import ru.itis.repositories.repo.UserRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostServiceImpl implements PostService{
    private PostRepository postRepository;
    private UserRepository userRepository;
    private SubscriptionRepository subscriptionRepository;
    private LikeRepository likeRepository;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, SubscriptionRepository subscriptionRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.likeRepository = likeRepository;
    }
    @Override
    public int createPostAndReturnId(PostDto postDto) throws SQLException {
        Post post = Post.builder()
                .text(postDto.getText())
                .user_id(postDto.getUser_id())
                .date(postDto.getDate())
                .build();
        return postRepository.saveAndReturnId(post);
    }

    @Override
    public List<PostDto> getAllByUser(int user_id, int my_id) throws SQLException {
        List<PostDto> posts = new ArrayList<>();
        List<Post> postUser = postRepository.findByUserId(user_id);

        for (Post post: postUser) {
            String authorUsername = userRepository.findUsernameById(post.getUser_id());
            boolean isLiked = likeRepository.isLiked(my_id, post.getId());
            PostDto postDto =PostDto.builder()
                    .id(post.getId())
                    .user_id(post.getUser_id())
                    .text(post.getText())
                    .date(post.getDate())
                    .authorUsername(authorUsername)
                    .liked(isLiked)
                    .build();
            posts.add(postDto);
            System.out.println(postDto.getUser_id()+ " " + postDto.getAuthorUsername());
        }

        return posts;
    }

    @Override
    public List<PostDto> getAllBySubscriptions(int user_id) throws SQLException {
        List<PostDto> posts = new ArrayList<>();
        List<Subscription> subUser = subscriptionRepository.getAllByUser(user_id);

        List<Post> allPosts = new ArrayList<>();

        for (Subscription subscription : subUser) {
            if (subscription.isFollow()) {
                List<Post> userPosts = postRepository.findByUserId(subscription.getFollowing_id());
                allPosts.addAll(userPosts);
            }
        }

        allPosts.sort((post1, post2) -> post2.getDate().compareTo(post1.getDate()));

        for (Post post: allPosts) {
            String authorUsername = userRepository.findUsernameById(post.getUser_id());
            boolean isLiked = likeRepository.isLiked(user_id, post.getId());
            posts.add(PostDto.builder()
                    .id(post.getId())
                    .user_id(post.getUser_id())
                    .text(post.getText())
                    .date(post.getDate())
                    .authorUsername(authorUsername)
                    .liked(isLiked)
                    .build());
        }
        return posts;

    }

    @Override
    public List<PostDto> getAll(int user_id) throws SQLException {
        List<PostDto> posts = new ArrayList<>();
        List<Post> allPosts = postRepository.findAll();

        for(Post post: allPosts) {
            String authorUsername = userRepository.findUsernameById(post.getUser_id());
            boolean isLiked = likeRepository.isLiked(user_id, post.getId());
            posts.add(PostDto.builder()
                    .id(post.getId())
                    .user_id(post.getUser_id())
                    .text(post.getText())
                    .date(post.getDate())
                    .authorUsername(authorUsername)
                    .liked(isLiked)
                    .build());
        }

        return posts;
    }

    @Override
    public PostDto findById(int id) throws SQLException {
        Post post = postRepository.findById(id);
        String authorUsername = userRepository.findUsernameById(post.getUser_id());

        PostDto postDto = PostDto.builder()
                .id(post.getId())
                .user_id(post.getUser_id())
                .text(post.getText())
                .date(post.getDate())
                .authorUsername(authorUsername)
                .build();
        return postDto;
    }
}
