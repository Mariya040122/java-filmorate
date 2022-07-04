package ru.yandex.practicum.filmorate.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.model.Model;

import java.util.ArrayList;
import java.util.List;

abstract class Controller<T extends Model>  {



     protected final Logger log = LoggerFactory.getLogger(this.getClass());

     private List<T> controllers = new ArrayList<>();


    public T create (T controller) throws ValidationException{
         controllers.add(controller);

         return controller;
     }

     public T put(T controller) throws ValidationException{
        T present = controllers.stream()
                .filter(c -> c.getId() == controller.getId())
                .findAny()
                .orElse(null);
        if (present != null){
            controllers.remove(present);
            controllers.add(controller);
            return controller;
        } else {
            throw new ValidationException("Такого d нет!");
        }
      }


     public  List<T> findAll(){
             return controllers;
         }
     }

