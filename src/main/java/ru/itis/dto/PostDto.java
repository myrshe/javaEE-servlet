package ru.itis.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class PostDto {
    private int id;
    private int user_id;
    private String text;
    private LocalDateTime date;
    private String authorUsername;
    private boolean liked;


}
