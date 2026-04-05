package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;


import java.util.Collection;
import java.util.List;

@Component
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.create(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(id);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.update(user);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        userRepository.addFriend(userId, friendId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        userRepository.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        return userRepository.getFriendsList(userId);
    }

    @Override
    public List<User> getMutualFriends(Long userId, Long otherId) {
        return userRepository.getMutualFriendsList(userId, otherId);
    }

}
