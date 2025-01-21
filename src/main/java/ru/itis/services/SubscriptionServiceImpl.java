package ru.itis.services;

import ru.itis.dto.SubscriptionDto;
import ru.itis.models.Subscription;
import ru.itis.repositories.repo.SubscriptionRepository;
import ru.itis.repositories.repo.UserRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionServiceImpl implements SubscriptionService{
    private UserRepository userRepository;
    private SubscriptionRepository subscriptionRepository;

    public SubscriptionServiceImpl(UserRepository userRepository, SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }
    @Override
    public List<SubscriptionDto> getAllByUser(int user_id) throws SQLException {
        List<SubscriptionDto> subscriptionDtos = new ArrayList<>();
        List<Subscription> subscriptions = subscriptionRepository.getAllByUser(user_id);
        for (Subscription subscription : subscriptions) {
            if (subscription.isFollow()) {
                String following_username = userRepository.findUsernameById(subscription.getFollowing_id());
                SubscriptionDto subscriptionDto = SubscriptionDto.builder()
                        .user_id(subscription.getUser_id())
                        .following_id(subscription.getFollowing_id())
                        .isFollowing(subscription.isFollow())
                        .following_username(following_username)
                        .build();
                subscriptionDtos.add(subscriptionDto);
            }
        }
        return subscriptionDtos;
    }

    @Override
    public void subscribe(SubscriptionDto subscriptionDto) throws SQLException {
        // Проверяем, существует ли уже подписка
        Subscription existingSubscription = subscriptionRepository.findByUserAndFollowing(
                subscriptionDto.getUser_id(), subscriptionDto.getFollowing_id());

        if (existingSubscription != null) {
            // Если подписка существует и isFollow = false, обновляем на true
            if (!existingSubscription.isFollow()) {
                existingSubscription.setFollow(true);
                subscriptionRepository.update(existingSubscription);
            }
        } else {
            // Если подписки нет, создаем новую запись
            Subscription newSubscription = Subscription.builder()
                    .user_id(subscriptionDto.getUser_id())
                    .following_id(subscriptionDto.getFollowing_id())
                    .isFollow(true)
                    .build();
            subscriptionRepository.save(newSubscription);
        }
    }

    @Override
    public void unSubscribe(SubscriptionDto subscriptionDto) throws SQLException {
        Subscription existingSubscription = subscriptionRepository.findByUserAndFollowing(
                subscriptionDto.getUser_id(), subscriptionDto.getFollowing_id());

        if (existingSubscription != null && existingSubscription.isFollow()) {
            existingSubscription.setFollow(false);
            subscriptionRepository.update(existingSubscription);
        }
    }

    @Override
    public boolean isSubscribed(int user_id, int following_id) throws SQLException {
        return subscriptionRepository.isSubscribed(user_id, following_id);
    }
}
