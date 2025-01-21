package ru.itis.repositories.repo;

import ru.itis.models.Post;
import ru.itis.models.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostRepositoryImpl implements PostRepository{

    private DataSource dataSource;

    private final static String SQL_INSERT_INTO_POST = "INSERT INTO posts(text,user_id,date) VALUES (?,?,?)";
    private final static String SQL_SELECT_TEXT_FROM_POST = "SELECT * FROM posts WHERE text = ?";
    private final static String SQL_SELECT_ID_FROM_POST = "SELECT * FROM posts WHERE id = ?";
    private final static String SQL_DELETE_ID_FROM_POST = "DELETE FROM posts WHERE id = ?";
    private final static String SQL_ALL_FROM_POST = "SELECT * FROM posts ORDER BY date DESC";
    private final static String SQL_ALL_BY_USER_ID = "SELECT * FROM posts WHERE user_id = ? ORDER BY date DESC";

    public PostRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Post> findAll() throws SQLException {
        List<Post> posts = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_ALL_FROM_POST);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                posts.add(Post.builder()
                        .id(resultSet.getInt("id"))
                        .text(resultSet.getString("text"))
                        .date(resultSet.getTimestamp("date").toLocalDateTime())
                        .user_id(resultSet.getInt("user_id"))
                        .build());
            }
        }
        return posts;
    }

    @Override
    public List<Post> findByUser(User entity) throws SQLException {
        List<Post> posts = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_ALL_BY_USER_ID)) {

            statement.setInt(1, entity.getId());
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    posts.add(Post.builder()
                            .id(resultSet.getInt("id"))
                            .text(resultSet.getString("text"))
                            .date(resultSet.getTimestamp("date").toLocalDateTime())
                            .user_id(resultSet.getInt("user_id"))
                            .build());
                }
            }
        }
        return posts;
    }

    @Override
    public List<Post> findByUserId(int id) throws SQLException {
        List<Post> posts = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_ALL_BY_USER_ID)) {


            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(Post.builder()
                            .id(resultSet.getInt("id"))
                            .text(resultSet.getString("text"))
                            .date(resultSet.getTimestamp("date").toLocalDateTime())
                            .user_id(resultSet.getInt("user_id"))
                            .build());
                }
            }
        }
        return posts;
    }



    @Override
    public Post findById(int id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ID_FROM_POST)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Post.builder()
                            .id(resultSet.getInt("id"))
                            .text(resultSet.getString("text"))
                            .date(resultSet.getTimestamp("date").toLocalDateTime())
                            .user_id(resultSet.getInt("user_id"))
                            .build();
                }
            }
        }
        return null;
    }

    @Override
    public int saveAndReturnId(Post entity) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT_INTO_POST, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, entity.getText());
            statement.setInt(2, entity.getUser_id());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getDate()));
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating post failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public void save(Post entity) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT_INTO_POST)) {
            statement.setString(1, entity.getText());
            statement.setInt(2, entity.getUser_id());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getDate()));
            statement.executeUpdate();
        }
    }

    @Override
    public void removeById(int id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE_ID_FROM_POST)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }
}
