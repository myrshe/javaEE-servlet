package ru.itis.repositories.repo;

import ru.itis.models.Post;
import ru.itis.models.User;
import ru.itis.repositories.Crud.CrudRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public interface PostRepository extends CrudRepository<Post> {
    //Optional<Post> findByText(String text) throws SQLException;
    List<Post> findAll() throws SQLException;
    //List<Post> findAllByUserID(Long id_user) throws SQLException;
    List<Post> findByUser(User entity) throws SQLException;
    List<Post> findByUserId(int id) throws SQLException;
    Post findById(int id) throws SQLException;
    int saveAndReturnId(Post entity) throws SQLException;
}
