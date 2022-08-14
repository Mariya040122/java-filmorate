package ru.yandex.practicum.filmorate.Storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.util.*;

@Component
@Qualifier("UserDatabase")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User add(User user) {
        if (user.getName() == null || user.getName().replace(" ", "").isEmpty()) {
            user.setName(user.getLogin());
        }
        String sqlQuery = "insert into \"USER\" (email, login, name, birthday) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        Long newId = keyHolder.getKey().longValue();

        if (user.getFriends() != null) {
            for (Long f : user.getFriends()) {
                String sqlFriend = "insert into friends (userid, friendid) " +
                        "values (?, ?)";
                jdbcTemplate.update(sqlFriend,
                        newId,
                        f);
            }
        }
        return find(newId);
    }

    @Override
    public User update(User user) {
        if (user.getName() == null || user.getName().replace(" ", "").isEmpty()) {
            user.setName(user.getLogin());
        }
        String sqlQuery = "update \"USER\" " +
                "set email = ?,  login = ?,  name = ?,  birthday = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , java.sql.Date.valueOf(user.getBirthday())
                , user.getId());

        String sqlDeleteFriends = "delete from friends where userid = ?";
        jdbcTemplate.update(sqlDeleteFriends
                , user.getId());

        if (user.getFriends() != null) {
            for (Long f : user.getFriends()) {
                String sqlAddFriends = "insert into friends (userid, friendid) " +
                        "values (?, ?)";
                jdbcTemplate.update(sqlAddFriends,
                        user.getId(),
                        f);
            }
        }
        return find(user.getId());
    }

    @Override
    public void delete(User user) {
        String sqlDeleteFriend = "delete from friends where userid = ? or friendid = ?";
        jdbcTemplate.update(sqlDeleteFriend
                , user.getId(), user.getId());

        String sqlDeleteUser = "delete from \"USER\" where id = ?";
        jdbcTemplate.update(sqlDeleteUser
                , user.getId());
    }

    @Override
    public User find(long id) {
        if (isExist(id)) {
            String sqlUser = "select u.name as name, " +
                    "u.email as email, " +
                    "u.login as login, " +
                    "u.birthday as birthday " +
                    "from \"USER\" as u " +
                    "where u.id = ?";
            User user = jdbcTemplate.queryForObject(sqlUser, new Object[]{id}, (rs, rowNum) ->
                    new User(
                            id,
                            rs.getString("email"),
                            rs.getString("login"),
                            rs.getString("name"),
                            rs.getDate("birthday").toLocalDate(),
                            new HashSet<>(getFriendsId(id))
                    ));
            return user;
        } else return null;
    }

    @Override
    public List<User> findAll() {

        String sqlUsers = "select u.id as id, " +
                "u.email as email, " +
                "u.login as login, " +
                "u.name as name, " +
                "u.birthday as birthday, " +
                "from \"USER\" as u ";
        List<User> users = jdbcTemplate.query(sqlUsers, (rs, rowNum) ->
                new User(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("name"),
                        rs.getDate("birthday").toLocalDate(),
                        getFriendsId(rs.getLong("id"))
                ));
        return users;
    }

    @Override
    public User addFriend(long id, long friendId) {
        String sqlFriend = "insert into friends (userid, friendid) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlFriend,
                id,
                friendId);
        return find(id);
    }

    @Override
    public User removeFriend(long id, long friendId) {
        String sqlDeleteFriend = "delete from friends where userid = ? and friendid = ?";
        jdbcTemplate.update(sqlDeleteFriend
                ,id
                ,friendId);
        return find(id);
    }

    @Override
    public List<User> getFriends(long id) {
        String sqlFriend = "SELECT u.id, u.email, u.login, u.name, u.birthday " +
                "FROM \"USER\" AS u " +
                "INNER JOIN FRIENDS AS f ON f.FRIENDID = u.ID " +
                "WHERE f.USERID = ?";
        List<User> users = jdbcTemplate.query(sqlFriend, (rs, rowNum) ->
                new User(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("name"),
                        rs.getDate("birthday").toLocalDate(),
                        getFriendsId(rs.getLong("id"))
                ), id);
        return users;
    }
    @Override
    public List<User> commonFriendsList(long id, long otherId) {
        String sqlFriend = "SELECT u.id, u.email, u.login, u.name, u.birthday " +
        "FROM \"USER\" AS u " +
        "INNER JOIN FRIENDS AS f ON f.FRIENDID = u.ID " +
        "INNER JOIN FRIENDS AS f1 ON f1.FRIENDID = u.ID "+
        "WHERE f.USERID = ? AND f1.USERID = ? ";
        List<User> users = jdbcTemplate.query(sqlFriend, (rs, rowNum) ->
                new User(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("name"),
                        rs.getDate("birthday").toLocalDate(),
                        getFriendsId(rs.getLong("id"))
                ), id, otherId);
        return users;
    }

    @Override
    public boolean isExist(long id) {
        String sqlUser = "select count(id) " +
                "from \"USER\" " +
                "where id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlUser, new Object[]{id}, Integer.class);
        return (count == 1);
    }

    private Set<Long> getFriendsId(long id){
        String sqlFriends = "select f.friendid as friendid " +
                "from friends as f " +
                "where f.userid = ?";
        List<Long> friends = jdbcTemplate.query(sqlFriends, (rs, rowNum) ->
                (rs.getLong("friendid")), id);
        return new HashSet<>(friends);
    }
}
