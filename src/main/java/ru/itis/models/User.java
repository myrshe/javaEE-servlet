package ru.itis.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String role;
}
