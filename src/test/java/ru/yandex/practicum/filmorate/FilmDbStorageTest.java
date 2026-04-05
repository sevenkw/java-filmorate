package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({
        FilmDbStorage.class,
        FilmRepository.class,
        FilmRowMapper.class,
        GenreRowMapper.class,
        UserDbStorage.class,
        UserRepository.class,
        UserRowMapper.class
})
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @Test
    void shouldCreateAndFindFilmById() {
        Film film = buildFilm("Film A", 120, 1, Set.of());
        Film created = filmDbStorage.createFilm(film);

        Film found = filmDbStorage.getFilmById(created.getId());

        assertThat(found.getId()).isEqualTo(created.getId());
        assertThat(found.getName()).isEqualTo("Film A");
        assertThat(found.getDuration()).isEqualTo(120);
    }

    @Test
    void shouldUpdateFilm() {
        Film film = buildFilm("Film A", 120, 1, Set.of());
        Film created = filmDbStorage.createFilm(film);

        Film updated = new Film();
        updated.setId(created.getId());
        updated.setName("Film B");
        updated.setDescription("Updated");
        updated.setReleaseDate(LocalDate.of(2000, 1, 1));
        updated.setDuration(150L);
        MpaRating mpa = new MpaRating();
        mpa.setId(1);
        updated.setMpa(mpa);

        filmDbStorage.updateFilm(updated);
        Film found = filmDbStorage.getFilmById(created.getId());

        assertThat(found.getName()).isEqualTo("Film B");
        assertThat(found.getDuration()).isEqualTo(150);
    }

    @Test
    void shouldAddAndRemoveLike() {
        Film film = buildFilm("Film A", 120, 1, Set.of());
        Film created = filmDbStorage.createFilm(film);

        User user = buildUser("u1@example.com", "u1");
        User createdUser = userDbStorage.createUser(user);

        filmDbStorage.addLike(created.getId(), createdUser.getId());
        Integer likesAfterAdd = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM film_likes WHERE film_id = ?",
                Integer.class,
                created.getId()
        );
        assertThat(likesAfterAdd).isEqualTo(1);

        filmDbStorage.removeLike(created.getId(), createdUser.getId());
        Integer likesAfterRemove = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM film_likes WHERE film_id = ?",
                Integer.class,
                created.getId()
        );
        assertThat(likesAfterRemove).isEqualTo(0);
    }

    @Test
    void shouldReturnPopularFilmsByLikes() {
        User u1 = userDbStorage.createUser(buildUser("a@example.com", "a"));
        User u2 = userDbStorage.createUser(buildUser("b@example.com", "b"));

        Film film1 = filmDbStorage.createFilm(buildFilm("Film A", 100, 1, Set.of()));
        Film film2 = filmDbStorage.createFilm(buildFilm("Film B", 90, 1, Set.of()));

        filmDbStorage.addLike(film1.getId(), u1.getId());
        filmDbStorage.addLike(film1.getId(), u2.getId());
        filmDbStorage.addLike(film2.getId(), u1.getId());

        List<Film> popular = filmDbStorage.getPopular(10);

        assertThat(popular.get(0).getId()).isEqualTo(film1.getId());
    }

    @Test
    void shouldStoreGenresForFilm() {
        Film film = buildFilm("Film A", 120, 1, Set.of(1L, 2L));
        Film created = filmDbStorage.createFilm(film);

        Film found = filmDbStorage.getFilmById(created.getId());

        assertThat(found.getGenres())
                .extracting(Genre::getId)
                .contains(1L, 2L);
    }

    private Film buildFilm(String name, int duration, int mpaId, Set<Long> genreIds) {
        Film film = new Film();
        film.setName(name);
        film.setDescription("Desc");
        film.setReleaseDate(LocalDate.of(1999, 12, 31));
        film.setDuration((long) duration);
        MpaRating mpa = new MpaRating();
        mpa.setId(mpaId);
        film.setMpa(mpa);

        if (genreIds != null && !genreIds.isEmpty()) {
            Set<Genre> genres = new LinkedHashSet<>();
            for (Long id : genreIds) {
                Genre genre = new Genre();
                genre.setId(id);
                genres.add(genre);
            }
            film.setGenres(genres);
        }
        return film;
    }

    private User buildUser(String email, String login) {
        User user = new User();
        user.setEmail(email);
        user.setLogin(login);
        user.setName(login);
        user.setBirthday(LocalDate.of(1990, 1, 1));
        return user;
    }
}
