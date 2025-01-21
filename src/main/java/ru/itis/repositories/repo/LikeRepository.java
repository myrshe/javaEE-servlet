package ru.itis.repositories.repo;


import java.sql.SQLException;

public interface LikeRepository{
    boolean isLiked(int user_id, int post_id) throws SQLException;
    void addLike(int user_id, int post_id) throws SQLException;
    void removeLike(int user_id, int post_id) throws SQLException;
}
