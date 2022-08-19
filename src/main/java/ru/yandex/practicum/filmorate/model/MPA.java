package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MPA {

    long id;

    String name;

    public MPA (long id){
        this.id = id;
    }

    public MPA (long id, String name){
        this.id = id;
        this.name = name;
    }


}
