package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MpaRatingRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Optional;

@Service
public class MpaService {
    private final MpaRatingRepository mpaRatingRepository;

    @Autowired
    public MpaService(MpaRatingRepository mpaRatingRepository) {
        this.mpaRatingRepository = mpaRatingRepository;
    }

    public List<MpaRating> getMpaRatings() {
        return mpaRatingRepository.findAll();
    }

    public MpaRating getMpaRatingById(int id) {
        return mpaRatingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг с id=" + id + " не найден"));
    }
}
