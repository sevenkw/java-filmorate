package ru.yandex.practicum.filmorate.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidator {
    private static final Logger logger = LoggerFactory.getLogger(UserValidator.class);

    public void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            logger.debug("Некорректный формат данных");
            throw new ValidationException("Адрес электронной почты должен содержать символ: " + " @");
        }

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            logger.debug("Некорректный формат данных");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            logger.info("Имя пользоавтеля установленно в соответствии с логином {} ", user.getLogin());
        }

        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            logger.debug("Некорректный формат данных");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
