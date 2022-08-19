package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Genre {

    long id;

    String name;

    public Genre (long id){
        this.id = id;
    }

    public Genre (long id, String name){
        this.id = id;
        this.name = name;
    }
}
