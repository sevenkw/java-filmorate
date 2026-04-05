package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRepository.class, UserRowMapper.class})
public class UserDbStorageTest {
    private final UserDbStorage userDbStorage;

    @Test
    void shouldCreateAndFindUserById() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("tester");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User created = userDbStorage.createUser(user);
        User found = userDbStorage.getUserById(created.getId());

        assertThat(found.getId()).isEqualTo(created.getId());
        assertThat(found.getEmail()).isEqualTo("test@example.com");
        assertThat(found.getLogin()).isEqualTo("tester");
    }

    @Test
    void updateUserTest() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("tester");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User created = userDbStorage.createUser(user);

        User userUpdated = new User();
        userUpdated.setId(created.getId());
        userUpdated.setEmail("anferov@example.com");
        userUpdated.setLogin("tester2");
        userUpdated.setName("Test User2");
        userUpdated.setBirthday(LocalDate.of(1992, 3, 7));

        User updated = userDbStorage.updateUser(userUpdated);

        assertThat(updated.getEmail()).isEqualTo("anferov@example.com");
        assertThat(updated.getLogin()).isEqualTo("tester2");
    }

    @Test
    void deleteUserTest() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("tester");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User created = userDbStorage.createUser(user);
        userDbStorage.deleteUser(created.getId());

        assertThatThrownBy(() -> userDbStorage.getUserById(created.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getAllUsersTest() {

        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("tester");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        userDbStorage.createUser(user);

        User user2 = new User();
        user2.setEmail("anfero@example.com");
        user2.setLogin("tester2");
        user2.setName("Test User2");
        user2.setBirthday(LocalDate.of(1995, 4, 8));

        userDbStorage.createUser(user2);

        Collection<User> users = userDbStorage.getAllUsers();

        assertThat(users).hasSize(2);
    }

    @Test
    void addFriendTest() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("tester");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        userDbStorage.createUser(user);

        User user2 = new User();
        user2.setEmail("anfero@example.com");
        user2.setLogin("tester2");
        user2.setName("Test User2");
        user2.setBirthday(LocalDate.of(1995, 4, 8));

        userDbStorage.createUser(user2);

        userDbStorage.addFriend(user.getId(), user2.getId());

        var listFriends = userDbStorage.getFriends(user.getId());
        for (User friend : listFriends) {
            assertThat(friend.getId()).isEqualTo(user2.getId());
        }
    }

    @Test
    void getMutualFriends() {
        User u1 = new User();
        u1.setEmail("u1@example.com");
        u1.setLogin("u1");
        u1.setName("User One");
        u1.setBirthday(LocalDate.of(1990, 1, 1));
        User created1 = userDbStorage.createUser(u1);

        User u2 = new User();
        u2.setEmail("u2@example.com");
        u2.setLogin("u2");
        u2.setName("User Two");
        u2.setBirthday(LocalDate.of(1991, 2, 2));
        User created2 = userDbStorage.createUser(u2);

        User u3 = new User();
        u3.setEmail("u3@example.com");
        u3.setLogin("u3");
        u3.setName("User Three");
        u3.setBirthday(LocalDate.of(1992, 3, 3));
        User created3 = userDbStorage.createUser(u3);

        userDbStorage.addFriend(created1.getId(), created3.getId());
        userDbStorage.addFriend(created2.getId(), created3.getId());


        assertThat(userDbStorage.getMutualFriends(created1.getId(), created2.getId()));
    }

}
