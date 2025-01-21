package ru.itis.services;

import ru.itis.dto.SignUpForm;

import java.sql.SQLException;

public interface SignUpService {
    void signUp(SignUpForm form) throws SQLException;
    boolean isUsernameExists(String username);
    boolean isEmailExists(String email);
}
