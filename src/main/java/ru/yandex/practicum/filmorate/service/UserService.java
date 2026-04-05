package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }

    public User updateUser(User newUser) {
        return userStorage.updateUser(newUser);
    }


    public User addFriends(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new ValidationException("Нельзя добавить в друзья себя самого");
        }

        userStorage.getUserById(userId);
        userStorage.getUserById(friendId);

        userStorage.addFriend(userId, friendId);
        return userStorage.getUserById(userId);
    }

    public User deleteFriends(Long userId, Long friendId) {

        if (userId.equals(friendId)) {
            throw new ValidationException("Нельзя удалить из друзей себя самого");
        }
        userStorage.getUserById(userId);
        userStorage.getUserById(friendId);

        userStorage.removeFriend(userId, friendId);
        return userStorage.getUserById(userId);
    }

    //возвращение списка пользователей являющихся его друзьями
    public List<User> getUserFriends(Long userId) {
        try {
            userStorage.getUserById(userId);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
        return userStorage.getFriends(userId);
    }

    //список друзей, общих с другим пользователем.
    public List<User> getMutualFriends(Long userId, Long otherUserId) {
        return userStorage.getMutualFriends(userId, otherUserId);
    }
}
