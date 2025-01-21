package ru.itis.repositories.repo;

import ru.itis.models.User;
import ru.itis.repositories.Crud.CrudRepository;

import java.sql.SQLException;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends CrudRepository<User> {
    Optional<User> findUserByUsername(String userName) throws SQLException;
    Optional<User> findUserByEmail(String email) throws SQLException;
    String findUsernameById(int id) throws SQLException;
    List<User> findAll() throws SQLException;
    Optional<User> findById(int id) throws SQLException;
}
