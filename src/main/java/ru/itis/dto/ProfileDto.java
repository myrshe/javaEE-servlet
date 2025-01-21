package ru.itis.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProfileDto {
    private int id;
    private String username;
    private String email;
}
