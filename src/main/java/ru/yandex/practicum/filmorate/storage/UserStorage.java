package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User createUser(User user);

    void deleteUser(Long id);

    User updateUser(User user);

    Collection<User> getAllUsers();

    User getUserById(Long id);
}
