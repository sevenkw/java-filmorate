package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepository extends BaseRepository<Genre> {
    private static final String FIND_ALL_GENRES = "SELECT * FROM genre";
    private static final String FIND_GENRE_BY_ID = "SELECT * FROM genre WHERE genre_id = ?";

    public GenreRepository(JdbcTemplate jdbcTemplate, RowMapper<Genre> rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    public List<Genre> findAll() {
        return findMany(FIND_ALL_GENRES);
    }

    public Optional<Genre> findById(int id) {
        return findOne(FIND_GENRE_BY_ID, id);
    }
}
