package ru.itis.repositories.repo;

import ru.itis.repositories.Crud.CrudRepository;

import java.sql.SQLException;
import java.util.Optional;
import ru.itis.models.Image;


public interface ImageRepository extends CrudRepository<Image> {
    Optional<Image> findById(int id) throws SQLException;

    Optional<Image> findIdByImage(String original_name, int post_id, Long size) throws SQLException;

    Optional<Image> findPostByImageId(int id) throws SQLException;
}
