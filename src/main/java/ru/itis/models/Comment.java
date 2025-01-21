package ru.itis.models;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class Comment {
    private int id;
    private String text;
    private int user_id;
    private int post_id;
    private LocalDateTime date;
}
