package ru.itis.services;

import ru.itis.dto.CommentDto;
import ru.itis.dto.PostDto;

import java.sql.SQLException;
import java.util.List;

public interface CommentService {
    List<CommentDto> findByPostId(int id) throws SQLException;
    void createComment(CommentDto commentDto) throws SQLException;
    List<CommentDto> allComments() throws SQLException;
    void removeComment (int id) throws SQLException;
}
