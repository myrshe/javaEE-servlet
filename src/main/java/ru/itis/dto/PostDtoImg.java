package ru.itis.dto;

import ru.itis.models.Image;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder


public class PostDtoImg {
    private PostDto postDto;
    private Image image;
}
