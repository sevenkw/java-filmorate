package ru.yandex.practicum.filmorate;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.FilmValidator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmValidationTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void create_valid_film() {
        Film film = new Film();
        film.setName("Film 1");
        film.setDescription("f".repeat(200));
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(1L);

        var result = validator.validate(film);
        assertTrue(result.isEmpty(), "Фильм должен быть валидным");
    }

    @Test
    void emptyName_shouldFailValidation() {
        Film film = new Film();
        film.setName(" ");
        film.setDescription("name");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(1L);

        var result = validator.validate(film);

        assertFalse(result.isEmpty(), "Фильм не может иметь пустое название");
    }

    @Test
    void nullName_shouldFailValidation() {
        Film film = new Film();
        film.setName(null);
        film.setDescription("name");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(1L);

        var result = validator.validate(film);
        assertFalse(result.isEmpty());
    }

    @Test
    void nullDescription_shouldFailValidation() {
        Film film = new Film();
        film.setName("Film 1");
        film.setDescription(null);
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(1L);

        var result = validator.validate(film);
        assertFalse(result.isEmpty());
    }

    @Test
    void description_length201() {
        Film film = new Film();
        film.setName("Film 1");
        film.setDescription("f".repeat(201));
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(1L);

        var result = validator.validate(film);
        assertFalse(result.isEmpty());
    }

    @Test
    void nullReleaseDate() {
        Film film = new Film();
        film.setName("Film 1");
        film.setDescription("good film");
        film.setReleaseDate(null);
        film.setDuration(1L);

        var result = validator.validate(film);
        assertFalse(result.isEmpty());
    }

    @Test
    void releaseDate_before_1895_12_28() {
        Film film = new Film();
        film.setName("Film 1");
        film.setDescription("good film");
        film.setReleaseDate(LocalDate.of(1894, 12, 26));
        film.setDuration(1L);

        FilmValidator filmValidator = new FilmValidator();

        assertThrows(ValidationException.class, () -> filmValidator.validateFilm(film));
    }

    @Test
    void nullDuration_shouldFailValidation() {
        Film film = new Film();
        film.setName("Film 1");
        film.setDescription("good film");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(null);

        var result = validator.validate(film);
        assertFalse(result.isEmpty());
    }

    @Test
    void zeroDuration_shouldFailValidation() {
        Film film = new Film();
        film.setName("Film 1");
        film.setDescription("good film");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(0L);

        var result = validator.validate(film);
        assertFalse(result.isEmpty());
    }

    @Test
    void negativeDuration_shouldFailValidation() {
        Film film = new Film();
        film.setName("Film 1");
        film.setDescription("good film");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(-5L);

        var result = validator.validate(film);
        assertFalse(result.isEmpty());
    }
}
