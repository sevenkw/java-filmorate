package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryFilmStorage implements FilmStorage {
    @Override
    public void addLike(Long filmId, Long userId) {

    }

    @Override
    public void removeLike(Long filmId, Long userId) {

    }

    @Override
    public List<Film> getPopular(int count) {
        return List.of();
    }

    private final Map<Long, Film> films = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    private final FilmValidator validator = new FilmValidator();

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film getFilmById(Long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
        return films.get(id);
    }

    @Override
    public Film createFilm(Film film) {
        validator.validateFilm(film);

        film.setId(getNextId());
        films.put(film.getId(), film);
        logger.info("Фильм успешно создан: {}", film);
        return film;

    }

    @Override
    public void deleteFilm(Long id) {
        if (id == null) {
            logger.debug("Некорректный формат данных");
            throw new ValidationException("Id должен быть указан");
        }

        if (films.containsKey(id)) {
            films.remove(id);
            logger.debug("Фильм удален");
        } else {
            throw new NotFoundException("Фильм c id = " + id + " не найден");
        }
    }

    @Override
    public Film updateFilm(Film newFilm) {
        if (newFilm.getId() == null) {
            logger.debug("Некорректный формат данных");
            throw new ValidationException("Id должен быть указан");
        }

        if (films.containsKey(newFilm.getId())) {
            var oldFilm = films.get(newFilm.getId());
            validator.validateFilm(newFilm);

            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());

            films.put(oldFilm.getId(), oldFilm);
            logger.info("Данные фильма {} успешно обновлены", oldFilm.getName());
            return oldFilm;
        }
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
