package ru.itis.models;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class Like {
    private Long id_user;
    private Long id_post;
    private boolean is_liked;
}
