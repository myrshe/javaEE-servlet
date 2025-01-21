package ru.itis.repositories.repo;

import ru.itis.models.Subscription;

import java.sql.SQLException;
import java.util.List;

public interface SubscriptionRepository{
    List<Subscription> getAllByUser(int user_id) throws SQLException;
    void save(Subscription entity) throws SQLException;
    void update(Subscription entity) throws SQLException;
    Subscription findByUserAndFollowing(int user_id, int following_id) throws SQLException;
    boolean isSubscribed(int user_id, int following_id) throws SQLException;
}
