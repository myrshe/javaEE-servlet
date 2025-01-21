package ru.itis.services;

import ru.itis.dto.ProfileDto;

import java.sql.SQLException;

public interface ProfileService {
    ProfileDto getProfile(String username, int id) throws SQLException;
    ProfileDto getProfileById(int id) throws SQLException;
}
