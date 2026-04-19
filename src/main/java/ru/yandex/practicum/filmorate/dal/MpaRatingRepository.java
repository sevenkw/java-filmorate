package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaRatingRepository extends BaseRepository<MpaRating> {
    private static final String FIND_ALL_MPA = "SELECT * FROM mpa";
    private static final String FIND_MPA_BY_ID = "SELECT * FROM mpa WHERE id = ?";

    public MpaRatingRepository(JdbcTemplate jdbc, RowMapper<MpaRating> mapper) {
        super(jdbc, mapper);
    }

    public List<MpaRating> findAll() {
        return findMany(FIND_ALL_MPA);
    }

    public Optional<MpaRating> findById(int id) {
        return findOne(FIND_MPA_BY_ID, id);
    }
}
