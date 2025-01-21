package ru.itis.services;

import ru.itis.dto.ProfileDto;
import ru.itis.models.Post;
import ru.itis.models.User;
import ru.itis.repositories.repo.PostRepository;
import ru.itis.repositories.repo.UserRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProfileServiceImpl implements ProfileService{
    private UserRepository userRepository;
    private PostRepository postRepository;

    public ProfileServiceImpl(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public ProfileDto getProfile(String username, int id) throws SQLException {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        List<Post> posts = postRepository.findByUserId(id);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            ProfileDto profileDto = ProfileDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();
            return profileDto;
        }
        return null;
    }

    @Override
    public ProfileDto getProfileById(int id) throws SQLException {
        Optional<User> userOptional = userRepository.findById(id);
        List<Post> posts = postRepository.findByUserId(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return ProfileDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();
        }
        return null;
    }
}
