package ru.itis.services;

import java.sql.SQLException;

public interface LikeService {
    boolean toggleLike(int user_id, int post_id) throws SQLException;
}
