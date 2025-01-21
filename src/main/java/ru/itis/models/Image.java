package ru.itis.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class Image {
    private int id;
    private String originalFileName;
    private String storageFileName;
    private Long size;
    private int postId;
    private String type;
}
