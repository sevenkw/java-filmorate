package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.enums.FriendshipStatus;

@Data
public class Friendship {
    private Long userId;
    private Long friendId;
    private FriendshipStatus status;
}
