package ru.itis.repositories.repo;

import ru.itis.models.Comment;
import ru.itis.models.Post;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentRepositoryImpl implements CommentRepository{

    private DataSource dataSource;

    private static final String SQL_INSERT_INTO_COMMENT = "INSERT INTO comments(text,user_id,post_id,date) VALUES (?,?,?,?)";
    private static final String SQL_SELECT_ID_FROM_COMMENT = "SELECT * FROM comments WHERE ID = ?";
    private static final String SQL_DELETE_ID_FROM_COMMENT = "DELETE FROM comments WHERE id = ?";
    private static final String SQL_ALL_BY_POST_ID = "SELECT * FROM comments WHERE post_id = ?";
    private static final String SQL_ALL = "SELECT * FROM comments";

    public CommentRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Comment entity) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT_INTO_COMMENT)) {

            statement.setString(1, entity.getText());
            statement.setInt(2, entity.getUser_id());
            statement.setInt(3, entity.getPost_id());
            statement.setTimestamp(4, Timestamp.valueOf(entity.getDate()));
            statement.executeUpdate();
        }
    }



    @Override
    public Optional<Comment> findById(int id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ID_FROM_COMMENT)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {

                    Comment comment = Comment.builder()
                            .id(resultSet.getInt("id"))
                            .text(resultSet.getString("text"))
                            .user_id(resultSet.getInt("user_id"))
                            .post_id(resultSet.getInt("post_id"))
                            .date(resultSet.getTimestamp("date").toLocalDateTime())
                            .build();

                    return Optional.ofNullable(comment);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void removeById(int id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE_ID_FROM_COMMENT)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public List<Comment> findByPost(Post entity) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_ALL_BY_POST_ID)) {

            statement.setInt(1, entity.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    comments.add(Comment.builder()
                            .id(resultSet.getInt("id"))
                            .text(resultSet.getString("text"))
                            .user_id(resultSet.getInt("user_id"))
                            .post_id(resultSet.getInt("post_id"))
                            .date(resultSet.getTimestamp("date").toLocalDateTime())
                            .build());
                }
            }
        }
        return comments;
    }

    @Override
    public List<Comment> findByPostId(int id) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_ALL_BY_POST_ID)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    comments.add(Comment.builder()
                            .id(resultSet.getInt("id"))
                            .text(resultSet.getString("text"))
                            .user_id(resultSet.getInt("user_id"))
                            .post_id(resultSet.getInt("post_id"))
                            .date(resultSet.getTimestamp("date").toLocalDateTime())
                            .build());
                }
            }
        }
        return comments;
    }

    @Override
    public List<Comment> findAll() throws SQLException {
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(SQL_ALL);
        ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                comments.add(Comment.builder()
                        .id(resultSet.getInt("id"))
                        .text(resultSet.getString("text"))
                        .user_id(resultSet.getInt("user_id"))
                        .post_id(resultSet.getInt("post_id"))
                        .date(resultSet.getTimestamp("date").toLocalDateTime())
                        .build());
            }
        }
        return comments;
    }
}
