package ru.itis.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.dto.SignInForm;
import ru.itis.dto.UserDto;
import ru.itis.models.User;
import ru.itis.repositories.repo.UserRepository;


import java.sql.SQLException;
import java.util.Optional;

public class SignInServiceImpl implements SignInService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public SignInServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserDto singIn(SignInForm signInForm) throws SQLException {
        Optional<User> userOptional = userRepository.findUserByUsername(signInForm.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (checkPassword(signInForm.getPassword(), user.getPassword())) {
                return UserDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .role(user.getRole())
                        .build();
            }
        } return null;
    }

    @Override
    public Boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}

