package ru.yandex.practicum.filmorate.Storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {

    User add(User user);

    User update(User user);

    void delete (User user);

    User find (long id);

    List<User> findAll ();

    User addFriend (long id,long friendId);

    User removeFriend (long id,long friendId);

    List<User> getFriends (long id);

    List<User> commonFriendsList (long id, long otherId);

    boolean isExist(long id);
}
