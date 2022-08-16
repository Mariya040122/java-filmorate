package ru.yandex.practicum.filmorate.Storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
@Qualifier("GenreDatabase")
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenres(){
        String sqlGenres = "select g.id as id, g.name as name from genre as g ";
        List<Genre> genres = jdbcTemplate.query(sqlGenres, (rs, rowNum) ->
                new Genre(rs.getLong("id"), rs.getString("name")));
        return genres;
    }

    @Override
    public Genre getGenresById(long id){
        String sqlCount = "select count(id) " +
                "from GENRE " +
                "where id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlCount, new Object[]{id}, Integer.class);
        if (count == 1) {
            String sqlGenres = "select id, name from genre " +
                    "where id = ?";
            Genre genre = jdbcTemplate.queryForObject(sqlGenres, new Object[]{id}, (rs, rowNum) ->
                    new Genre(
                            id,
                            rs.getString("name")
                    ));
            return genre;
        } else return null;
    }
}
