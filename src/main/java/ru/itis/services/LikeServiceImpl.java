package ru.itis.services;

import ru.itis.repositories.repo.LikeRepository;

import java.sql.SQLException;

public class LikeServiceImpl implements LikeService{
    private LikeRepository likeRepository;

    public LikeServiceImpl(LikeRepository likeRepository){
        this.likeRepository = likeRepository;
    }
    @Override
    public boolean toggleLike(int user_id, int post_id) throws SQLException {
        boolean isLiked = likeRepository.isLiked(user_id, post_id);

        if (isLiked) {
            // если уже лайкнуто, убираем
            likeRepository.removeLike(user_id, post_id);
        } else {
            // если лайка нет, лайкаем
            likeRepository.addLike(user_id, post_id);
        }
        return !isLiked;
    }
}
