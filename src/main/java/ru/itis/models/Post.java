package ru.itis.models;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class Post {
    private int id;
    private String text;
    private LocalDateTime date;
    private int user_id;
}
