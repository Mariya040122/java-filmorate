package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.Storage.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("01.Добавления пользователя")
    public void addUserTest() {
        User user1 = new User(0, "user1@test.com", "login", "",
                LocalDate.of(1954, 12, 31), new HashSet<>());
        User addedUser1 = userStorage.add(user1);
        assertThat(addedUser1)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "login");
        User user2 = new User(0, "user2@test.com", "vasya", "Василий",
                LocalDate.of(1983, 01, 19), new HashSet<>());
        User addedUser2 = userStorage.add(user2);
        assertThat(addedUser2)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("name", "Василий")
                .hasFieldOrPropertyWithValue("login", "vasya");
    }
    @Test
    @DisplayName("02.Обновление пользователя")
    public void updateUserTest(){
        User user1 = new User(1, "user1@test.com", "peter", "Петр",
                LocalDate.of(1954, 12, 31), new HashSet<>());
        User updateUser1 = userStorage.update(user1);
        assertThat(updateUser1)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Петр")
                .hasFieldOrPropertyWithValue("login", "peter");
    }

    @Test
    @DisplayName("03.Извлечение пользователя")
    public void findUserTest(){
        User findUser1 = userStorage.find(1L);
        assertThat(findUser1)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Петр")
                .hasFieldOrPropertyWithValue("login", "peter");
    }
    @Test
    @DisplayName("04.Извлечение списка пользователей")
    public void findAllUserTest(){
        List<User> findAllUser = userStorage.findAll();
        assertThat(findAllUser)
                .isNotNull()
                .hasSize(2)
                .extracting(User::getId)
                .containsExactly(1L, 2L);
        assertThat(findAllUser.get(0))
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Петр")
                .hasFieldOrPropertyWithValue("login", "peter");
        assertThat(findAllUser.get(1))
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("name", "Василий")
                .hasFieldOrPropertyWithValue("login", "vasya");
    }
    @Test
    @DisplayName("05.Удаление пользователя")
    public void deleteUserTest(){
        User user1 = new User(1, "user1@test.com", "peter", "Петр",
                LocalDate.of(1954, 12, 31), new HashSet<>());
        userStorage.delete(user1);
        User deleteUser1 = userStorage.find(1L);
        assertThat(deleteUser1)
                .isNull();
    }


    @Test
    @DisplayName("06.Добавление друга")
    public void addFriendUserTest(){
        User user3 = new User(0, "user3@test.com", "archi", "Арчибальд",
                LocalDate.of(1924, 10, 13), new HashSet<>());
        User addedUser3 = userStorage.add(user3);
        User user4 = new User(0, "user4@test.com", "angel", "Анжела",
                LocalDate.of(2000, 01, 20), new HashSet<>());
        User addedUser4 = userStorage.add(user4);
        User user = userStorage.find(2L);
        assertThat(user.getFriends())
                .isEmpty();
        userStorage.addFriend(user.getId(), addedUser3.getId());
        userStorage.addFriend(user.getId(), addedUser4.getId());
        user = userStorage.find(2L);
        assertThat(user.getFriends())
                .isNotEmpty()
                .hasSize(2);
        assertThat(user.getFriends().contains(addedUser3.getId()))
                .isTrue();
        assertThat(user.getFriends().contains(addedUser4.getId()))
                .isTrue();
    }
    @Test
    @DisplayName("07.Вывод друзей")
    public void getFriendsUserTest(){
        userStorage.addFriend(3L, 2L);
        userStorage.addFriend(3L, 4L);
        List<User>Friends = userStorage.getFriends(3L);
        assertThat(Friends)
                .isNotNull()
                .hasSize(2)
                .extracting(User::getId)
                .containsExactly(2L, 4L);
        assertThat(Friends.get(0))
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("name", "Василий")
                .hasFieldOrPropertyWithValue("login", "vasya");
        assertThat(Friends.get(1))
                .hasFieldOrPropertyWithValue("id", 4L)
                .hasFieldOrPropertyWithValue("name", "Анжела")
                .hasFieldOrPropertyWithValue("login", "angel");

    }
    @Test
    @DisplayName("08.Удаление друга")
    public void removeFriendUserTest(){
        userStorage.removeFriend(3L, 2L);
        List<User>Friends = userStorage.getFriends(3L);
        assertThat(Friends)
                .isNotNull()
                .hasSize(1)
                .extracting(User::getId)
                .containsExactly( 4L);
        assertThat(Friends.get(0))
                .hasFieldOrPropertyWithValue("id", 4L)
                .hasFieldOrPropertyWithValue("name", "Анжела")
                .hasFieldOrPropertyWithValue("login", "angel");
    }

    @Test
    @DisplayName("09.Получение общих друзей")
    public void commonFriendsListUserTest(){
        List<User>Friends = userStorage.commonFriendsList(3L, 2L);
        assertThat(Friends)
                .isNotNull()
                .hasSize(1)
                .extracting(User::getId)
                .containsExactly( 4L);
        assertThat(Friends.get(0))
                .hasFieldOrPropertyWithValue("id", 4L)
                .hasFieldOrPropertyWithValue("name", "Анжела")
                .hasFieldOrPropertyWithValue("login", "angel");
    }
    @Test
    @DisplayName("10.Проверка существования пользователя")
    public void isExistTest(){
        assertThat(userStorage.isExist(2L))
                .isTrue();
        assertThat(userStorage.isExist(-1L))
                .isFalse();
    }
}
