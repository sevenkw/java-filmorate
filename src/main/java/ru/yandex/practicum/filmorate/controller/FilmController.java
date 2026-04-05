package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmRequestDto;
import ru.yandex.practicum.filmorate.dto.FilmResponseDto;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Validated
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<FilmResponseDto> getAllFilms() {
        Collection<Film> films = filmService.getAllFilms();
        Collection<FilmResponseDto> filmResponseDtos = new ArrayList<>();
        for (Film f : films) {
            filmResponseDtos.add(FilmMapper.toDto(f));
        }
        return filmResponseDtos;
    }

    @PostMapping
    public FilmResponseDto createFilm(@Valid @RequestBody FilmRequestDto dto) {
        Film film = FilmMapper.toFilm(dto);
        Film filmSaved = filmService.createFilm(film);
        return FilmMapper.toDto(filmSaved);
    }

    @PutMapping
    public FilmResponseDto updateFilm(@Valid @RequestBody FilmRequestDto newFilm) {
        Film film = FilmMapper.toFilm(newFilm);
        Film filmSaved = filmService.updateFilm(film);
        return FilmMapper.toDto(filmSaved);
    }


    @GetMapping("/{id}")
    public FilmResponseDto getFilmById(@PathVariable @Positive Long id) {
        Film film = filmService.getFilmById(id);
        return FilmMapper.toDto(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public FilmResponseDto putFilmLike(@PathVariable @Positive Long id, @Positive @PathVariable Long userId) {
        Film film = filmService.putLikeOnFilm(id, userId);
        return FilmMapper.toDto(film);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public FilmResponseDto deleteFilmLike(@PathVariable @Positive Long id, @Positive @PathVariable Long userId) {
        Film film = filmService.deleteLikeOnFilm(id, userId);
        return FilmMapper.toDto(film);
    }

    @GetMapping("/popular")
    public List<FilmResponseDto> getPopularFilms(@RequestParam(defaultValue = "10") @Positive int count) {
        List<Film> film = filmService.getFilmsByTopLikes(count);
        List<FilmResponseDto> finalFilms = new ArrayList<>();
        for (Film f : film) {
            finalFilms.add(FilmMapper.toDto(f));
        }
        return finalFilms;
    }
}
