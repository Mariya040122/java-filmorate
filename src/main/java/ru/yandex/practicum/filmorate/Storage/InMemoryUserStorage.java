package ru.yandex.practicum.filmorate.Storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    private List<User> users = new ArrayList<>();

    @Override
    public User add(User user) {
        users.add(user);
        if (user.getName() == null || user.getName().replace(" ", "").isEmpty()) {
            user.setName(user.getLogin());
        }
        return user;
    }

    @Override
    public User update(User user) {
        User present = users.stream()
                .filter(c -> c.getId() == user.getId())
                .findAny()
                .orElse(null);
        if (present != null) {
            users.remove(present);
            users.add(user);
            return user;
        } else return null;

    }

    @Override
    public void delete(User user) {
        User present = users.stream()
                .filter(c -> c.getId() == user.getId())
                .findFirst()
                .orElse(null);
        users.remove(user);
    }

    @Override
    public User find(long id) {
        User present = users.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
        return present;
    }

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public User addFriend(long id, long friendId) {
        User user = users.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);

        User friend = users.stream()
                .filter(c -> c.getId() == friendId)
                .findFirst()
                .orElse(null);

        if (user != null && friend != null) {
            if (user.getFriends() == null) {
                user.setFriends(new HashSet<>());
            }
            user.addFriend(friendId);
            if (friend.getFriends() == null) {
                friend.setFriends(new HashSet<>());
            }
            friend.addFriend(id);
            return user;
        } else return null;
    }

    @Override
    public User removeFriend(long id, long friendId) {
        User user = users.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);

        User friend = users.stream()
                .filter(c -> c.getId() == friendId)
                .findFirst()
                .orElse(null);

        if (user != null && friend != null) {
            if (user.getFriends() == null) {
                user.setFriends(new HashSet<>());
            }
            user.removeFriend(friendId);
            if (friend.getFriends() == null) {
                friend.setFriends(new HashSet<>());
            }
            friend.removeFriend(id);
            return user;
        } else return null;
    }

    @Override
    public List<User> getFriends(long id) {
        User user = users.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
        if (user != null) {
             Set<Long> friends = user.getFriends();
             return users.stream()
                     .filter(u -> friends.contains(u.getId()))
                     .collect(Collectors.toList());
        } else return null;
    }

    @Override
    public List<User> commonFriendsList(long id, long otherId) {
        User user = users.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);

        User otherUser = users.stream()
                .filter(c -> c.getId() == otherId)
                .findFirst()
                .orElse(null);

        if (user != null && otherUser != null) {
            if (user.getFriends() == null) {
                user.setFriends(new HashSet<>());
            }
            if (otherUser.getFriends() == null) {
                otherUser.setFriends(new HashSet<>());
            }
            Set<Long> userFriends = user.getFriends().stream().collect(Collectors.toSet());
            userFriends.removeAll(otherUser.getFriends());
            Set<Long> result = user.getFriends().stream().collect(Collectors.toSet());
            result.removeAll(userFriends);
            return users.stream()
                    .filter(u -> result.contains(u.getId()))
                    .collect(Collectors.toList());
        } else return null;
    }

    @Override
    public boolean isExist(long id){
        return (!users.stream()
                .filter(u -> u.getId() == id)
                .collect(Collectors.toList()).isEmpty());
    }
}
