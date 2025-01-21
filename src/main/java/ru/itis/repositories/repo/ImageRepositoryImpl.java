package ru.itis.repositories.repo;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import ru.itis.models.Image;

public class ImageRepositoryImpl implements ImageRepository{

    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    private static final String SQL_SELECT_ID_FROM_IMAGES = "SELECT * FROM images WHERE id = ?";
    private static final String SQL_INSERT = "INSERT INTO images(original_file_name, storage_file_name, type, post_id, size)" + "values (?, ?, ?, ?, ?)";
    private static final String SQL_DELETE_FROM_IMAGES = "DELETE FROM images WHERE ID = ?";
    private static final String SQL_SELECT_POST_ID_FROM_IMAGES = "SELECT * FROM images WHERE post_id = ?";
    private static final String SQL_SELECT_BY_PARAM = "SELECT * FROM images WHERE original_file_name = ? and post_id = ? and size = ? ORDER BY id DESC limit 1";

    public ImageRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public Optional<Image> findById(int id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ID_FROM_IMAGES)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {

                    return Optional.of(Image.builder()
                            .id(resultSet.getInt("id"))
                            .originalFileName(resultSet.getString("original_file_name"))
                            .storageFileName(resultSet.getString("storage_file_name"))
                            .size(resultSet.getLong("size"))
                            .postId(resultSet.getInt("post_id"))
                            .type(resultSet.getString("type"))
                            .build());
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Image> findIdByImage(String original_name, int post_id, Long size) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_PARAM)) {

            statement.setString(1, original_name);
            statement.setInt(2, post_id);
            statement.setLong(3, size);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(Image.builder()
                            .id(resultSet.getInt("id"))
                            .originalFileName(resultSet.getString("original_file_name"))
                            .storageFileName(resultSet.getString("storage_file_name"))
                            .size(resultSet.getLong("size"))
                            .postId(resultSet.getInt("post_id"))
                            .type(resultSet.getString("type"))
                            .build());
                }
            }
        }
        return Optional.empty();
    }




    @Override
    public Optional<Image> findPostByImageId(int id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_POST_ID_FROM_IMAGES)) {


            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(Image.builder()
                            .id(resultSet.getInt("id"))
                            .originalFileName(resultSet.getString("original_file_name"))
                            .storageFileName(resultSet.getString("storage_file_name"))
                            .size(resultSet.getLong("size"))
                            .postId(resultSet.getInt("post_id"))
                            .type(resultSet.getString("type"))
                            .build());
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void save(Image entity) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {
            statement.setString(1, entity.getOriginalFileName());
            statement.setString(2, entity.getStorageFileName());
            statement.setString(3, entity.getType());
            statement.setInt(4, entity.getPostId());
            statement.setLong(5, entity.getSize());
            statement.executeUpdate();
        }
    }



    @Override
    public void removeById(int id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE_FROM_IMAGES)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
