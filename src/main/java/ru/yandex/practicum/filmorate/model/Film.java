package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Film {

    long id;
    String name;
    String description;
    LocalDate releaseDate;
    long duration;

    public Film (long id, String name, String description, LocalDate releaseDate, long duration){
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

}
