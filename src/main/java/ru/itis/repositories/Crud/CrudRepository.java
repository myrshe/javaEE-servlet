package ru.itis.repositories.Crud;

import java.sql.SQLException;
import java.util.Optional;

public interface CrudRepository<T> {
    void save(T entity) throws SQLException;

    void removeById(int id) throws  SQLException;
}
