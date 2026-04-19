package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.FilmRequestDto;
import ru.yandex.practicum.filmorate.dto.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {

    public static Film toFilm(FilmRequestDto dto) {
        Film film = new Film();
        film.setId(dto.getId());
        film.setName(dto.getName());
        film.setDescription(dto.getDescription());
        film.setReleaseDate(dto.getReleaseDate());
        film.setDuration(dto.getDuration());

        film.setMpa(dto.getMpa());

        if (dto.getGenres() != null) {
            LinkedHashMap<Long, Genre> unique = new LinkedHashMap<>();
            for (Genre genre : dto.getGenres()) {
                if (genre == null || genre.getId() == null || genre.getId() < 1) {
                    throw new ValidationException("Invalid genre id " + (genre == null ? null : genre.getId()));
                }
                unique.putIfAbsent(genre.getId(), genre);
            }
            film.setGenres(new LinkedHashSet<>(unique.values()));
        } else {
            film.setGenres(new LinkedHashSet<>());
        }

        return film;
    }

    public static FilmResponseDto toDto(Film film) {
        FilmResponseDto dto = new FilmResponseDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());
        dto.setMpa(film.getMpa());
        if (film.getGenres() == null) {
            dto.setGenres(List.of());
        } else {
            dto.setGenres(film.getGenres().stream()
                    .map(FilmMapper::toGenreDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private static GenreDto toGenreDto(Genre genre) {
        GenreDto dto = new GenreDto();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        return dto;
    }
}
