package ru.itis.services;

import ru.itis.dto.SubscriptionDto;

import java.sql.SQLException;
import java.util.List;

public interface SubscriptionService {
    List<SubscriptionDto> getAllByUser(int user_id) throws SQLException;
    void subscribe(SubscriptionDto subscriptionDto) throws SQLException;
    void unSubscribe(SubscriptionDto subscriptionDto) throws SQLException;
    boolean isSubscribed(int user_id, int following_id) throws SQLException;
}
