package ru.yandex.practicum.filmorate.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidator {
    private static final Logger logger = LoggerFactory.getLogger(FilmValidator.class);

    public void validateFilm(Film film) {

        if (film.getName() == null || film.getName().isBlank()) {
            logger.debug("Некорректный формат данных");
            throw new ValidationException("Название не может быть пустым");
        }

        if (film.getDescription() == null || film.getDescription().length() > 200) {
            logger.debug("Некорректный формат данных");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            logger.debug("Некорректный формат данных");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() == null || film.getDuration() <= 0) {
            logger.debug("Некорректный формат данных");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }
}
