package ru.itis.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.dto.SignUpForm;
import ru.itis.models.User;
import ru.itis.repositories.repo.UserRepository;

import java.sql.SQLException;

public class SignUpServiceImpl implements SignUpService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public SignUpServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    @Override
    public void signUp(SignUpForm form) throws SQLException {
        User user = User.builder()
                .username(form.getUsername())
                .password(passwordEncoder.encode(form.getPassword()))
                .email(form.getEmail())
                .build();
        userRepository.save(user);
    }

    @Override
    public boolean isUsernameExists(String username) {
        try {
            return userRepository.findUserByUsername(username).isPresent();
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean isEmailExists(String email) {
        try {
            return userRepository.findUserByEmail(email).isPresent();
        } catch (SQLException e) {
            return false;
        }
    }

}
