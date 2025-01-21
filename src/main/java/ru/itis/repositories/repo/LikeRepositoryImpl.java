package ru.itis.repositories.repo;

import ru.itis.models.Like;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LikeRepositoryImpl implements LikeRepository{
    private DataSource dataSource;

    public LikeRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static final String SQL_ISLIKED= "SELECT isLiked FROM likes WHERE user_id = ? AND post_id = ?";
    private static final String SQL_ADD_LIKE = "INSERT INTO likes (user_id, post_id, isLiked) VALUES (?, ?, true) " +
            "ON CONFLICT (user_id, post_id) DO UPDATE SET isLiked = true";
    private static final String SQL_REMOVE_LIKE = "UPDATE likes SET isLiked = false WHERE user_id = ? AND post_id = ?";
    @Override
    public boolean isLiked(int user_id, int post_id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_ISLIKED)) {


            statement.setInt(1, user_id);
            statement.setInt(2, post_id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() && resultSet.getBoolean("isLiked");
        }
    }

    @Override
    public void addLike(int user_id, int post_id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_ADD_LIKE)) {

            statement.setInt(1, user_id);
            statement.setInt(2, post_id);
            statement.executeUpdate();
        }
    }


    @Override
    public void removeLike(int user_id, int post_id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_REMOVE_LIKE)) {

            statement.setInt(1, user_id);
            statement.setInt(2, post_id);
            statement.executeUpdate();
        }
    }

}
