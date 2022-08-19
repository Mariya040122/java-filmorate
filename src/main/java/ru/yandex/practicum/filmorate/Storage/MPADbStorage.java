package ru.yandex.practicum.filmorate.Storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

@Component
@Qualifier("MPADatabase")
public class MPADbStorage implements MPAStorage {

    private final JdbcTemplate jdbcTemplate;

    public MPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MPA> getMPA (){
        String sqlMPA = "select g.id as id, g.name as name from ratings as g ";
        List<MPA> ratings = jdbcTemplate.query(sqlMPA, (rs, rowNum) ->
                new MPA(rs.getLong("id"), rs.getString("name")));
        return ratings;
    }

    @Override
    public MPA getMPAById(long id){
        String sqlCount = "select count(id) " +
                "from RATINGS " +
                "where id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlCount, new Object[]{id}, Integer.class);
        if (count == 1) {
            String sqlMPA = "select id, name from ratings " +
                    "where id = ?";
            MPA mpa = jdbcTemplate.queryForObject(sqlMPA, new Object[]{id}, (rs, rowNum) ->
                    new MPA(
                            id,
                            rs.getString("name")
                    ));
            return mpa;
        } else return null;
    }
}
