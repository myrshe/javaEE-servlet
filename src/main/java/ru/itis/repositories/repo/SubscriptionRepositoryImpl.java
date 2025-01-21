package ru.itis.repositories.repo;

import ru.itis.models.Subscription;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionRepositoryImpl implements SubscriptionRepository{
    private DataSource dataSource;

    private static final String SQL_SELECT_FROM_SUB_BY_USERID_AND_FOLLOWINGID = "SELECT * FROM subscriptions WHERE user_id = ? AND following_id = ?";
    private static final String SQL_SELECT_FROM_SUB_BY_USERID = "SELECT * FROM subscriptions WHERE user_id = ?";
    private static final String SQL_INSERT_SUB = "INSERT INTO subscriptions(user_id,following_id,isFollow) VALUES(?,?,?)";
    private static final String SQL_UPDATE_SUB = "UPDATE subscriptions SET isFollow = ? WHERE user_id = ? AND following_id = ?";

    public SubscriptionRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public List<Subscription> getAllByUser(int user_id) throws SQLException {
        List<Subscription> subscriptions = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_FROM_SUB_BY_USERID)) {

            statement.setInt(1, user_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    subscriptions.add(Subscription.builder()
                            .user_id(resultSet.getInt("user_id"))
                            .following_id(resultSet.getInt("following_id"))
                            .isFollow(resultSet.getBoolean("isFollow"))
                            .build());
                }
            }
        }
        return subscriptions;
    }


    @Override
    public void save(Subscription subscription) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT_SUB)) {

            statement.setInt(1, subscription.getUser_id());
            statement.setInt(2, subscription.getFollowing_id());
            statement.setBoolean(3, subscription.isFollow());
            statement.executeUpdate();
        }
    }

    @Override
    public void update(Subscription subscription) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_SUB)) {

            statement.setBoolean(1, subscription.isFollow());
            statement.setInt(2, subscription.getUser_id());
            statement.setInt(3, subscription.getFollowing_id());
            statement.executeUpdate();
        }
    }

    @Override
    public Subscription findByUserAndFollowing(int user_id, int following_id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_FROM_SUB_BY_USERID_AND_FOLLOWINGID)) {


            statement.setInt(1, user_id);
            statement.setInt(2, following_id);

            try (ResultSet resultSet = statement.executeQuery()) {


                if (resultSet.next()) {
                    return Subscription.builder()
                            .user_id(resultSet.getInt("user_id"))
                            .following_id(resultSet.getInt("following_id"))
                            .isFollow(resultSet.getBoolean("isFollow"))
                            .build();
                }
            }
        }
        return null;
    }

    @Override
    public boolean isSubscribed(int user_id, int following_id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_FROM_SUB_BY_USERID_AND_FOLLOWINGID)) {

            statement.setInt(1, user_id);
            statement.setInt(2, following_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean("isFollow");
                }
            }
        }
        return false;
    }
}
