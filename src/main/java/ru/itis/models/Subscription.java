package ru.itis.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class Subscription {
    private  int user_id;
    private int following_id;
    private boolean isFollow;
}
