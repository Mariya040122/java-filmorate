package ru.yandex.practicum.filmorate.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Storage.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
public class UserService  {

    private long id = 1;
    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage){
        this.storage = storage;
    }

    public User add(User user){
        user.setId(id);
        id++;
        return storage.add(user);
    }

    public User update(User user){
        return storage.update(user);
    }

    public void delete (User user){
        storage.delete(user);
    }

    public User find(long id){
        return storage.find(id);
    }

    public List<User> findAll (){
        return storage.findAll();
    }

    public User addFriend (long id,long friendId){
        return storage.addFriend(id, friendId);
    }

    public User removeFriend (long id,long friendId){
        return storage.removeFriend(id, friendId);
    }

    public List<User> getFriends(long id){
        return storage.getFriends(id);
    }

    public List<User> commonFriendsList (long id, long otherId){
        return storage.commonFriendsList(id,otherId);
    }

    public boolean isExist(long id){
        return storage.isExist(id);
    }
}
