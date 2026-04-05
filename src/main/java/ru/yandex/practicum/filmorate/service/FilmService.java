package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidator;


import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreService genreService;
    private final MpaService mpaService;
    private final FilmValidator filmValidator = new FilmValidator();

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage,
                       GenreService genreService, MpaService mpaService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreService = genreService;
        this.mpaService = mpaService;
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film createFilm(Film film) {
        validateFilmReferences(film);
        filmValidator.validateFilm(film);
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        validateFilmReferences(film);
        filmValidator.validateFilm(film);
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public Film putLikeOnFilm(Long filmId, Long userId) {
        filmStorage.getFilmById(filmId);
        userStorage.getUserById(userId);

        filmStorage.addLike(filmId, userId);
        return getFilmById(filmId);
    }

    public Film deleteLikeOnFilm(Long filmId, Long userId) {
        filmStorage.getFilmById(filmId);
        userStorage.getUserById(userId);

        filmStorage.removeLike(filmId, userId);
        return getFilmById(filmId);
    }

    public List<Film> getFilmsByTopLikes(int count) {
        if (count <= 0) {
            throw new ValidationException("Количество не должно быть отрицательным или 0");
        }
        return filmStorage.getPopular(count);
    }

    private void validateFilmReferences(Film film) {
        if (film.getMpa() == null || film.getMpa().getId() <= 0) {
            throw new ValidationException("Рейтинг должен быть указан");
        }
        film.setMpa(mpaService.getMpaRatingById(film.getMpa().getId()));

        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }
        Map<Long, Genre> unique = new LinkedHashMap<>();
        for (Genre genre : film.getGenres()) {
            if (genre == null || genre.getId() == null || genre.getId() < 1) {
                throw new ValidationException("Invalid genre id " + (genre == null ? null : genre.getId()));
            }
            Genre loaded = genreService.findById(genre.getId().intValue());
            unique.putIfAbsent(loaded.getId(), loaded);
        }
        film.setGenres(new LinkedHashSet<>(unique.values()));
    }
}
