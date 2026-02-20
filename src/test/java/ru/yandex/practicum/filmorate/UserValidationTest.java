package ru.yandex.practicum.filmorate;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.UserValidator;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validateUser() {
        User user = new User();
        user.setEmail("yaroslavanferov@yandex.ru");
        user.setLogin("yaroslavanferov");
        user.setName("Yaroslav");
        user.setBirthday(LocalDate.of(2005, 7, 8));

        var result = validator.validate(user);
        assertTrue(result.isEmpty());
    }

    @Test
    void validateUser_NoName() {
        User user = new User();
        user.setEmail("yaroslavanferov@yandex.ru");
        user.setLogin("yaroslavanferov");
        user.setBirthday(LocalDate.of(2005, 7, 8));

        UserValidator userValidator = new UserValidator();
        userValidator.validateUser(user);

        assertEquals("yaroslavanferov", user.getName());

    }

    @Test
    void nullEmail() {
        User user = new User();
        user.setEmail(null);
        user.setLogin("yaroslavanferov");
        user.setBirthday(LocalDate.of(2005, 7, 8));

        var result = validator.validate(user);
        assertFalse(result.isEmpty());
    }

    @Test
    void emptyEmail() {
        User user = new User();
        user.setEmail("");
        user.setLogin("yaroslavanferov");
        user.setBirthday(LocalDate.of(2005, 7, 8));

        var result = validator.validate(user);
        assertFalse(result.isEmpty());
    }

    @Test
    void emailWithoutSymbol() {
        User user = new User();
        user.setEmail("yaroslavanferov.ru");
        user.setLogin("yaroslavanferov");
        user.setBirthday(LocalDate.of(2005, 7, 8));

        var result = validator.validate(user);
        assertFalse(result.isEmpty());
    }

    @Test
    void nullLogin() {
        User user = new User();
        user.setEmail("yaroslavanferov@yandex.ru");
        user.setLogin(null);
        user.setName("Yaroslav");
        user.setBirthday(LocalDate.of(2005, 7, 8));

        var result = validator.validate(user);
        assertFalse(result.isEmpty());
    }

    @Test
    void emptyLogin() {
        User user = new User();
        user.setEmail("yaroslavanferov@yandex.ru");
        user.setLogin("");
        user.setName("Yaroslav");
        user.setBirthday(LocalDate.of(2005, 7, 8));

        var result = validator.validate(user);
        assertFalse(result.isEmpty());
    }
    @Test
    void loginHasWhitespace() {
        User user = new User();
        user.setEmail("yaroslavanferov@yandex.ru");
        user.setLogin("yaroslav anferov");
        user.setName("Yaroslav");
        user.setBirthday(LocalDate.of(2005, 7, 8));

        UserValidator userValidator = new UserValidator();
        assertThrows(ValidationException.class, () -> userValidator.validateUser(user));
    }

    @Test
    void birthdayIsNull() {
        User user = new User();
        user.setEmail("yaroslavanferov@yandex.ru");
        user.setLogin("yaroslavanferov");
        user.setName("Yaroslav");
        user.setBirthday(null);

        UserValidator userValidator = new UserValidator();
        assertThrows(ValidationException.class, () -> userValidator.validateUser(user));
    }

    @Test
    void birthdayInTheFuture() {
        User user = new User();
        user.setEmail("yaroslavanferov@yandex.ru");
        user.setLogin("yaroslavanferov");
        user.setName("Yaroslav");
        user.setBirthday(LocalDate.now().plusDays(1));

        UserValidator userValidator = new UserValidator();
        assertThrows(ValidationException.class, () -> userValidator.validateUser(user));
    }


}
