package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
public class UserRepository extends BaseRepository<User> {
    private static final String FIND_ALL_USERS = "SELECT * FROM users";
    private static final String CREATE_USER = "INSERT INTO users (email, login, name, birthday) values (?, ?, ?, ?)";
    private static final String DELETE_USER = "DELETE FROM users WHERE user_id = ?";
    private static final String UPDATE_USER = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? where user_id = ?";
    private static final String FIND_BY_ID = "SELECT * FROM users WHERE user_id =?";
    private static final String ADD_FRIEND = "INSERT INTO friends (user_id, friend_id, status_id) VALUES (?, ?, 1)";
    private static final String DELETE_FRIENDS = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_ALL_FRIENDS =
            "SELECT u.* FROM users AS u JOIN friends AS f ON u.user_id=f.friend_id WHERE f.user_id = ?";


    public UserRepository(JdbcTemplate jdbcTemplate, RowMapper<User> rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    public List<User> findAll() {
        return findMany(FIND_ALL_USERS);
    }

    public User create(User user) {
        Long id = insert(CREATE_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        user.setId(id);
        return user;
    }

    public void delete(Long id) {
        if (findById(id).isEmpty()) {
            throw new NotFoundException("Вы не можете удалить несуществующего пользователя");
        }
        jdbc.update(DELETE_USER, id);
    }

    public User update(User user) {
        update(UPDATE_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        return user;
    }

    public Optional<User> findById(Long id) {
        return findOne(FIND_BY_ID, id);
    }


    public void addFriend(Long userId, Long friendId) {
        jdbc.update(ADD_FRIEND, userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        jdbc.update(DELETE_FRIENDS, userId, friendId);
    }


    public List<User> getFriendsList(Long userId) {
        return findMany(FIND_ALL_FRIENDS, userId);
    }

    public List<User> getMutualFriendsList(Long userId, Long friendId) {
        List<User> usersFriends = getFriendsList(userId);
        List<User> othersFriends = getFriendsList(friendId);

        Set<Long> othersIds = new HashSet<>();
        for (User user : othersFriends) {
            Long id = user.getId();
            othersIds.add(id);
        }

        List<User> mutualFriends = new ArrayList<>();
        for (User user : usersFriends) {
            if (othersIds.contains(user.getId())) {
                mutualFriends.add(user);
            }
        }
        return mutualFriends;
    }
}

