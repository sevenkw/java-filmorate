package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

//для post и put
@Data
public class FilmRequestDto {
    private Long id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @NotNull(message = "Описание не может быть пустым")
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;
    @NotNull private LocalDate releaseDate;
    @NotNull(message = "Продолжительность фильма должна быть указана")
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private Long duration;
    @NotNull(message = "Рейтинг должен быть указан")
    private MpaRating mpa;
    private List<Genre> genres;
}
