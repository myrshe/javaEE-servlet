package ru.itis.repositories.repo;

import ru.itis.models.User;

import javax.sql.DataSource;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository{

    private DataSource dataSource;

    private static final String SQL_SELECT_USERNAME_FROM_USERS = "SELECT * FROM users WHERE username = ?";
    private static final String SQL_SELECT_ID_FROM_USERS = "SELECT * FROM users WHERE id = ?";
    private static final String SQL_INSERT_INTO_USERS = "INSERT INTO users(username,password,email) VALUES (?,?,?)";
    private static final String SQL_DELETE_ID_FROM_USERS = "DELETE FROM users WHERE id = ?";
    private static final String SQL_ALL_FROM_USER = "SELECT * FROM users";
    private static final String SQL_SELECT_EMAIL_FROM_USERS = "SELECT * FROM users WHERE email = ?";

    public UserRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<User> findUserByUsername(String userName) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_USERNAME_FROM_USERS)) {


            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String role = resultSet.getString("role");
                User user = new User(id, username, password, email, role);
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findUserByEmail(String email) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_EMAIL_FROM_USERS)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("email"),
                            resultSet.getString("role")
                    );
                    return Optional.of(user);
                }
            }
        }
        return Optional.empty();
    }


    @Override
    public String findUsernameById(int id) throws SQLException{
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ID_FROM_USERS)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getString("username");
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() throws SQLException{
        List<User> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_ALL_FROM_USER)) {

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                users.add(User.builder()
                        .id(resultSet.getInt("id"))
                        .username(resultSet.getString("username"))
                        .password(resultSet.getString("password"))
                        .email(resultSet.getString("email"))
                        .role(resultSet.getString("role"))
                        .build());
            }
        }
        return users;
    }


    @Override
    public void save(User entity) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT_INTO_USERS)) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getPassword());
            statement.setString(3, entity.getEmail());
            statement.executeUpdate();
        }

    }

    @Override
    public Optional<User> findById(int id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ID_FROM_USERS)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = User.builder()
                        .id(resultSet.getInt("id"))
                        .username(resultSet.getString("username"))
                        .password(resultSet.getString("password"))
                        .email(resultSet.getString("email"))
                        .role(resultSet.getString("role"))
                        .build();
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }


    @Override
    public void removeById(int id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_DELETE_ID_FROM_USERS)) {
            statement.setInt(1,id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
