package ru.itis.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class SubscriptionDto {
    private int user_id;
    private int following_id;
    private boolean isFollowing;
    private String following_username;
}
