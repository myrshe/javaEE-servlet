package ru.itis.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import ru.itis.models.Image;
public interface ImageService {
    void saveFileToStorage(InputStream file, String original_file_name, String content_type, int post_id, Long size);

    void writeFileFromStorage(int file_id, OutputStream outputStream);

    Image getFileInfo(int file_id);

    void deleteImage(int file_id) throws SQLException, IOException;

    Image getImageByPost(int post_id) throws SQLException;

    Image getImageByParam(String original_name, int post_id, Long size) throws SQLException;
}
