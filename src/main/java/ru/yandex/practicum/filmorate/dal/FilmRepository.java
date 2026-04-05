package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class FilmRepository extends BaseRepository<Film> {
    private static final String FIND_ALL_FILMS =
            "SELECT f.*, m.id AS mpa_id, m.name AS mpa_name " +
                    "FROM films f LEFT JOIN mpa m ON f.mpa_id = m.id";

    private static final String FIND_BY_ID =
            "SELECT f.*, m.id AS mpa_id, m.name AS mpa_name " +
                    "FROM films f LEFT JOIN mpa m ON f.mpa_id = m.id " +
                    "WHERE f.film_id = ?";

    private static final String CREATE_FILM =
            "INSERT INTO films (name, mpa_id, description, release_date, duration) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_FILM =
            "UPDATE films SET name = ?, mpa_id = ?, description = ?, release_date = ?, duration = ? " +
                    "WHERE film_id = ?";

    private static final String DELETE_FILM =
            "DELETE FROM films WHERE film_id = ?";

    private static final String PUT_LIKE_ON_FILM =
            "INSERT INTO film_likes(film_id, user_id) VALUES (?, ?)";

    private static final String DELETE_LIKE_ON_FILM =
            "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";

    private static final String FIND_POPULAR_FILM =
            "SELECT f.*, m.id AS mpa_id, m.name AS mpa_name, COUNT(fl.user_id) AS likes " +
                    "FROM films f " +
                    "LEFT JOIN mpa m ON f.mpa_id = m.id " +
                    "LEFT JOIN film_likes fl ON f.film_id = fl.film_id " +
                    "GROUP BY f.film_id, m.id, m.name " +
                    "ORDER BY likes DESC " +
                    "LIMIT ?";

    private static final String DELETE_GENRE = " DELETE FROM film_genre WHERE film_id = ?";
    private static final String ADD_FILM_GENRE = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
    private static final String GET_GENRES_ON_FILM =
            "SELECT g.genre_id, g.genre_name\n" +
                    "  FROM genre g\n" +
                    "  JOIN film_genre fg ON g.genre_id = fg.genre_id\n" +
                    "  WHERE fg.film_id = ?";


    public FilmRepository(JdbcTemplate jdbcTemplate, RowMapper<Film> rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    public List<Film> findAll() {
        return findMany(FIND_ALL_FILMS);
    }

    public Optional<Film> findById(Long id) {
        return findOne(FIND_BY_ID, id);
    }

    public Film create(Film film) {
        Long id = insert(CREATE_FILM,
                film.getName(),
                film.getMpa().getId(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration()
        );
        film.setId(id);
        return film;
    }

    public Film update(Film film) {
        update(UPDATE_FILM,
                film.getName(),
                film.getMpa().getId(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());

        return film;
    }

    public void delete(Long id) {
        if (findById(id).isEmpty()) {
            throw new NotFoundException("Вы не можете удалить несуществующий фильм");
        }
        delete(DELETE_FILM, id);
    }

    public void putLikeOnFilm(Long filmId, Long userId) {
        jdbc.update(PUT_LIKE_ON_FILM, filmId, userId);
    }

    public void deleteLikeOnFilm(Long filmId, Long userId) {
        jdbc.update(DELETE_LIKE_ON_FILM, filmId, userId);
    }


    public List<Film> getPopularListFilms(int count) {
        return findMany(FIND_POPULAR_FILM, count);
    }

    public void deleteGenresOnFilm(Long filmId) {
        jdbc.update(DELETE_GENRE, filmId);
    }

    public void setGenresOnFilm(long filmId, Set<Long> genreIds) {
        for (Long genreId : genreIds) {
            jdbc.update(ADD_FILM_GENRE, filmId, genreId);
        }
    }

    public List<Genre> getGenres(long filmId) {
        return jdbc.query(GET_GENRES_ON_FILM, new GenreRowMapper(), filmId);
    }
}
