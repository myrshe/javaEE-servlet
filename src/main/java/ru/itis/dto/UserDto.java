package ru.itis.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class UserDto {
    private int id;
    private String username;
    private String role;
}
