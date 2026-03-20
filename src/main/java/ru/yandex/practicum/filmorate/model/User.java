package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен содержать символ @")
    private String email;
    @NotBlank(message = "Логин не может быть пустым")
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;

}
