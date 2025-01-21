package ru.itis.services;

import ru.itis.dto.SignInForm;
import ru.itis.dto.UserDto;

import java.sql.SQLException;

public interface SignInService {
    UserDto singIn(SignInForm signInForm) throws SQLException;

    Boolean checkPassword(String rawPassword, String encodedPassword);

}
