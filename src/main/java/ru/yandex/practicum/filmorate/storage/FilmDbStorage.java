package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final FilmRepository filmRepository;

    @Override
    public Film createFilm(Film film) {
        Film created = filmRepository.create(film);
        setGenresIfPresent(created);
        return getFilmById(created.getId());
    }

    @Override
    public void deleteFilm(Long id) {
        filmRepository.delete(id);
    }

    @Override
    public Film updateFilm(Film film) {
        Film updated = filmRepository.update(film);
        filmRepository.deleteGenresOnFilm(updated.getId());
        setGenresIfPresent(updated);
        return getFilmById(updated.getId());
    }

    @Override
    public Collection<Film> getAllFilms() {
        List<Film> films = filmRepository.findAll();
        for (Film film : films) {
            attachGenres(film);
        }
        return films;
    }

    @Override
    public Film getFilmById(Long id) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + id + " не найден"));
        attachGenres(film);
        return film;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        filmRepository.putLikeOnFilm(filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        filmRepository.deleteLikeOnFilm(filmId, userId);
    }

    @Override
    public List<Film> getPopular(int count) {
        List<Film> films = filmRepository.getPopularListFilms(count);
        for (Film film : films) {
            attachGenres(film);
        }
        return films;
    }

    private void setGenresIfPresent(Film film) {
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }
        Set<Long> genreIds = new LinkedHashSet<>();
        for (Genre genre : film.getGenres()) {
            if (genre.getId() != null) {
                genreIds.add(genre.getId());
            }
        }
        if (!genreIds.isEmpty()) {
            filmRepository.setGenresOnFilm(film.getId(), genreIds);
        }
    }

    private void attachGenres(Film film) {
        List<Genre> genres = filmRepository.getGenres(film.getId());
        film.setGenres(new LinkedHashSet<>(genres));
    }
}
