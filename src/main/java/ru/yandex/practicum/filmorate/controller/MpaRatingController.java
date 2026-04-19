package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaRatingController {
    private final MpaService mpaService;

    @Autowired
    public MpaRatingController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping()
    public List<MpaRating> getAllMpaRatings() {
        return mpaService.getMpaRatings();
    }

    @GetMapping("/{id}")
    public MpaRating getMpaRatingById(@PathVariable("id") int id) {
        return mpaService.getMpaRatingById(id);
    }
}
