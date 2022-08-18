package ru.yandex.practicum.filmorate.Storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public List<Genre> getFilmGenreByFilmId(long filmId) {
        String sqlGenre = "select gf.genre_id as id, g.name as name from genrefilm as gf " +
                "inner join genre as g on g.id=gf.genre_id " +
                "where gf.film_id=?";
        List<Genre> genres = jdbcTemplate.query(sqlGenre, (rs, rowNum) ->
                new Genre(rs.getLong("id"), rs.getString("name")), filmId);
        return genres;
    }

    @Override
    public List<Genre> getAllFilmsGenre() {
        String sqlGenre = "select gf.genre_id as id, g.name as name from genrefilm as gf " +
                "inner join genre as g on g.id=gf.genre_id ";
        List<Genre> genres = jdbcTemplate.query(sqlGenre, (rs, rowNum) ->
                new Genre(rs.getLong("id"), rs.getString("name")));
        return genres;
    }
    @Override
    public List<Genre> addFilmGenres(long filmId, List<Genre> genres){
        Set<Long> ids = new HashSet<>();
        for (Genre genre : genres) {
            if (!ids.contains(genre.getId())) {
                String sqlGenre = "insert into genrefilm (film_id, genre_id) " +
                        "values (?, ?)";
                jdbcTemplate.update(sqlGenre,
                        filmId,
                        genre.getId());
                ids.add(genre.getId());
            }
        }
        return getFilmGenreByFilmId(filmId);

    }
    @Override
    public void deleteFilmGenres(long filmId){
        String sqlDeleteGenre = "delete from genrefilm where film_id = ?";
        jdbcTemplate.update(sqlDeleteGenre
                , filmId);

    }


}
