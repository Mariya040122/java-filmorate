package ru.yandex.practicum.filmorate.Validator;


import ru.yandex.practicum.filmorate.Validator.LumiereDate;

import javax.validation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LumiereDateValidator implements ConstraintValidator<LumiereDate, LocalDate>{
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value != null && (value.isAfter(LocalDate.parse("28.12.1895", DateTimeFormatter.ofPattern("dd.MM.yyyy"))))){
            return true;
        }
        return false;
    }
}
