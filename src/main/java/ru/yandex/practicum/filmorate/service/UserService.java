package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        return user;
    }

    public User deleteFriends(Long userId, Long friendId) {

        if (userId.equals(friendId)) {
            throw new ValidationException("Нельзя удалить из друзей себя самого");
        }

        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);

        return user;
    }

    //возвращение списка пользователей являющихся его друзьями
    public List<User> getUserFriends(Long userId) {
        User user = userStorage.getUserById(userId);
        Set<Long> friendsId = user.getFriends();

        List<User> userFriends = new ArrayList<>();
        for (var realUser : friendsId) {
            User u = userStorage.getUserById(realUser);
            userFriends.add(u);
        }
        return userFriends;
    }

    //список друзей, общих с другим пользователем.
    public List<User> getMutualFriends(Long userId, Long otherUserId) {
        List<User> userMutualFriends = new ArrayList<>();

        User user = userStorage.getUserById(userId);
        Set<Long> friendsId = user.getFriends();

        User friend = userStorage.getUserById(otherUserId);
        Set<Long> listId = friend.getFriends();

        Set<Long> otherUserFriends = new HashSet<>();
        for (var identicalUsers : friendsId) {
            if (listId.contains(identicalUsers)) {
                otherUserFriends.add(identicalUsers);
            }
        }

        for (var realUsers : otherUserFriends) {
            User u = userStorage.getUserById(realUsers);
            userMutualFriends.add(u);
        }

        return userMutualFriends;
    }
}
