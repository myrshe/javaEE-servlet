package ru.itis.services;

import ru.itis.dto.CommentDto;
import ru.itis.models.Comment;
import ru.itis.repositories.repo.CommentRepository;
import ru.itis.repositories.repo.UserRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentServiceImpl implements CommentService{
    private CommentRepository commentRepository;
    private UserRepository userRepository;

    public CommentServiceImpl(UserRepository userRepository, CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }
    @Override
    public List<CommentDto> findByPostId(int id) throws SQLException {
        List<CommentDto> comments = new ArrayList<>();
        List<Comment> commentsOfPost = commentRepository.findByPostId(id);
        for (Comment comment: commentsOfPost) {
            String authorUsername = userRepository.findUsernameById(comment.getUser_id());
            CommentDto commentDto = CommentDto.builder()
                    .id(comment.getId())
                    .text(comment.getText())
                    .user_id(comment.getUser_id())
                    .post_id(comment.getPost_id())
                    .date(comment.getDate())
                    .authorUsername(authorUsername)
                    .build();
            comments.add(commentDto);
        }
        return comments;
    }

    @Override
    public void createComment(CommentDto commentDto) throws SQLException {
        Comment comment = Comment.builder()
                .text(commentDto.getText())
                .user_id(commentDto.getUser_id())
                .post_id(commentDto.getPost_id())
                .date(commentDto.getDate())
                .build();
        commentRepository.save(comment);
    }

    @Override
    public List<CommentDto> allComments() throws SQLException {
        List<CommentDto> commentDtos = new ArrayList<>();
        List<Comment> comments = commentRepository.findAll();
        for (Comment comment: comments) {
            String authorUsername = userRepository.findUsernameById(comment.getUser_id());
            CommentDto commentDto = CommentDto.builder()
                    .id(comment.getId())
                    .text(comment.getText())
                    .user_id(comment.getUser_id())
                    .post_id(comment.getPost_id())
                    .date(comment.getDate())
                    .authorUsername(authorUsername)
                    .build();
            commentDtos.add(commentDto);
        }
        return commentDtos;
    }

    @Override
    public void removeComment(int id) throws SQLException {
        commentRepository.removeById(id);
    }
}
