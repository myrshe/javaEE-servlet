package ru.itis.repositories.repo;

import ru.itis.models.Comment;
import ru.itis.models.Post;
import ru.itis.repositories.Crud.CrudRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends CrudRepository<Comment> {

    List<Comment> findByPost (Post entity) throws SQLException;
    Optional<Comment> findById(int id) throws SQLException;
    List<Comment> findByPostId (int id) throws  SQLException;
    List<Comment> findAll () throws SQLException;
}
