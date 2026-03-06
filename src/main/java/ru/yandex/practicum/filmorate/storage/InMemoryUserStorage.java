package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.UserValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(InMemoryUserStorage.class);
    private final UserValidator userValidation = new UserValidator();

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User getUserById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
        return user;
    }

    @Override
    public User createUser(User user) {
        userValidation.validateUser(user);

        user.setId(getNextId());
        users.put(user.getId(), user);
        logger.info("Пользователь успешно создан");
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        if (id == null) {
            logger.debug("Некорректный формат данных");
            throw new ValidationException("Id должен быть указан");
        }
        if (users.containsKey(id)) {
            users.remove(id);
            logger.debug("Пользователь удален");
        } else {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }

    @Override
    public User updateUser(User newUser) {
        if (newUser.getId() == null) {
            logger.debug("Некорректный формат данных");
            throw new ValidationException("Id должен быть указан");
        }

        if (users.containsKey(newUser.getId())) {
            var oldUser = users.get(newUser.getId());
            userValidation.validateUser(newUser);

            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());

            users.put(oldUser.getId(), oldUser);
            logger.info("Данные пользователя {} были успешно обновлены", oldUser.getId());
            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
