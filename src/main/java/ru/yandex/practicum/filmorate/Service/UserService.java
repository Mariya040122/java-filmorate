package ru.yandex.practicum.filmorate.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Storage.UserStorage;
import ru.yandex.practicum.filmorate.controller.NotFoundException;
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

    public User update(User user) throws NotFoundException{
        if (storage.isExist(user.getId())) {
            return storage.update(user);
        } else throw new NotFoundException("Ошибка, при получении запроса на изменение данных пользователя");
    }

    public void delete (User user){
        storage.delete(user);
    }

    public User find(long id) throws NotFoundException {
        if (storage.isExist(id)) {
            return storage.find(id);
        } else throw new NotFoundException ("Ошибка, при получении запроса на вывод данных одного пользователя");
    }

    public List<User> findAll (){
        return storage.findAll();
    }

    public User addFriend (long id,long friendId) throws NotFoundException{
        if (storage.isExist(id) && storage.isExist(friendId)) {
            return storage.addFriend(id, friendId);
        } else throw new NotFoundException ("Ошибка, при получении запроса на добавление в список друзей");
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
