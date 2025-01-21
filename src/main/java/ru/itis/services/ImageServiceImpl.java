package ru.itis.services;

import org.springframework.jdbc.core.RowMapper;
import ru.itis.repositories.repo.ImageRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import ru.itis.models.Image;

public class ImageServiceImpl implements ImageService{
    private ImageRepository imageRepository;

    private static final String IMAGE_STORAGE_PATH = "C://programming//oris//SemWork//src//main//webapp//image/";

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    private RowMapper<Image> fileRowMapper = (row, rowNumber) ->
            Image.builder()
                    .id(row.getInt("id"))
                    .originalFileName(row.getString("original_file_name"))
                    .storageFileName(row.getString("storage_file_name"))
                    .type(row.getString("type"))
                    .postId(row.getInt("post_id"))
                    .size(row.getLong("size"))
                    .build();

    @Override
    public void saveFileToStorage(InputStream file, String originalFileName, String contentType, int post_id, Long size) {
        Image image = Image.builder()
                .originalFileName(originalFileName)
                .storageFileName(UUID.randomUUID().toString())
                .size(size)
                .postId(post_id)
                .type(contentType)
                .build();

        try {
            Files.copy(file, Paths.get(IMAGE_STORAGE_PATH + image.getStorageFileName() + "." + image.getType().split("/")[1]));
            imageRepository.save(image);

        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to save file to storage: " + e.getMessage(), e);
        }
    }

    @Override
    public void writeFileFromStorage(int fileId, OutputStream outputStream) {
        Image image = null;
        try {
            Optional<Image> imageOptional = imageRepository.findById(fileId);
            if (imageOptional.isPresent()) {
                image = imageOptional.get();
                File file = new File(IMAGE_STORAGE_PATH + image.getStorageFileName() + "." + image.getType().split("/")[1]);
                Files.copy(file.toPath(), outputStream);
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Image getFileInfo(int fileId) {
        try {
            Optional<Image> imageOptional = imageRepository.findById(fileId);
            if (imageOptional.isPresent()) {
                return imageOptional.get();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void deleteImage(int fileId) throws SQLException, IOException {
        Optional<Image> imageOptional = imageRepository.findById(fileId);
        if (imageOptional.isPresent()) {
            Image image = imageOptional.get();
            imageRepository.removeById(fileId);
            String filePath = IMAGE_STORAGE_PATH + image.getStorageFileName() + "." + image.getType().split("/")[1];
            Files.delete(Paths.get(filePath));
        }
    }

    @Override
    public Image getImageByPost(int post_id) throws SQLException {
        Optional<Image> imageOptional = imageRepository.findPostByImageId(post_id);
        if (imageOptional.isPresent()) {
            return imageOptional.get();
        }
        return null;
    }

    @Override
    public Image getImageByParam(String originalName, int post_id, Long size) throws SQLException {
        Optional<Image> imageOptional = imageRepository.findIdByImage(originalName, post_id, size);
        if (imageOptional.isPresent()) {
            return imageOptional.get();
        }
        return null;
    }
}
