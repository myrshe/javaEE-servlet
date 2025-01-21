package ru.itis.services;

import ru.itis.dto.PostDto;

import java.sql.SQLException;
import java.util.List;

public interface PostService {
    int createPostAndReturnId(PostDto postDto) throws SQLException;
    List<PostDto> getAllByUser(int user_id, int my_id) throws SQLException;
    List<PostDto> getAll(int user_id) throws SQLException;
    PostDto findById(int id) throws SQLException;
    List<PostDto> getAllBySubscriptions(int user_id) throws SQLException;
}
