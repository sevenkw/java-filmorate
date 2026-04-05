package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserRequestDto;
import ru.yandex.practicum.filmorate.dto.UserResponseDto;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<UserResponseDto> getAllUsers() {
        Collection<User> us = userService.getAllUsers();
        Collection<UserResponseDto> userResponseDtos = new ArrayList<>();
        for (User u : us) {
            userResponseDtos.add(UserMapper.toDto(u));
        }
        return userResponseDtos;
    }

    @PostMapping
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto user) {
        User newUser = UserMapper.toUser(user);
        User newUserSaved = userService.createUser(newUser);
        return UserMapper.toDto(newUserSaved);
    }

    @PutMapping
    public UserResponseDto updateUser(@Valid @RequestBody UserRequestDto newUser) {
        User user = UserMapper.toUser(newUser);
        User newUserSaved = userService.updateUser(user);
        return UserMapper.toDto(newUserSaved);
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable @Positive Long id) {
        User user = userService.getUserById(id);
        return UserMapper.toDto(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public UserResponseDto addFriend(@PathVariable @Positive Long id, @Positive @PathVariable Long friendId) {
        User user = userService.addFriends(id, friendId);
        return UserMapper.toDto(user);

    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public UserResponseDto deleteFriend(@PathVariable @Positive Long id, @Positive @PathVariable Long friendId) {
        User user = userService.deleteFriends(id, friendId);
        return UserMapper.toDto(user);
    }

    @GetMapping("/{id}/friends")
    public List<UserResponseDto> getFriends(@PathVariable @Positive Long id) {
        List<User> user = userService.getUserFriends(id);
        List<UserResponseDto> userResponseDtos = new ArrayList<>();
        for (User u : user) {
            userResponseDtos.add(UserMapper.toDto(u));
        }
        return userResponseDtos;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<UserResponseDto> getMutualFriends(@PathVariable @Positive Long id, @Positive @PathVariable Long otherId) {
        List<User> users = userService.getMutualFriends(id, otherId);
        List<UserResponseDto> userResponseDtos = new ArrayList<>();
        for (User u : users) {
            userResponseDtos.add(UserMapper.toDto(u));
        }
        return userResponseDtos;
    }
}
