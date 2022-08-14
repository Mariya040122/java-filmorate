package ru.yandex.practicum.filmorate.Storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Qualifier("FilmDatabase")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film add(Film film) {
        String sqlQuery = "insert into film (name,  description, releaseDate, duration, mpa) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        Long newId = keyHolder.getKey().longValue();

        if (film.getGenres() != null) {
            Set<Long> ids = new HashSet<>();
            for (Genre genre : film.getGenres()) {
                if (!ids.contains(genre.getId())) {
                    String sqlGenre = "insert into genrefilm (film_id, genre_id) " +
                            "values (?, ?)";
                    jdbcTemplate.update(sqlGenre,
                            newId,
                            genre.getId());
                    ids.add(genre.getId());
                }
            }
        }

        if (film.getLikes() != null) {
            for (Long like : film.getLikes()) {
                String sqlLike = "insert into likes (film_id, user_id) " +
                        "values (?, ?)";
                jdbcTemplate.update(sqlLike,
                        newId,
                        like);
            }
        }
        return find(newId);
    }

    @Override
    public Film update(Film film) {
        if (isExist(film.getId())) {
            String sqlQuery = "update film " +
                    "set name = ?,  description = ?,  releaseDate = ?,  duration = ?, mpa = ? " +
                    "WHERE id = ?";
            jdbcTemplate.update(sqlQuery
                    , film.getName()
                    , film.getDescription()
                    , java.sql.Date.valueOf(film.getReleaseDate())
                    , film.getDuration()
                    , film.getMpa().getId()
                    , film.getId());

            String sqlDeleteGenre = "delete from genrefilm where film_id = ?";
            jdbcTemplate.update(sqlDeleteGenre
                    , film.getId());

            if (film.getGenres() != null) {
                Set<Long> ids = new HashSet<>();
                for (Genre genre : film.getGenres()) {
                    if (!ids.contains(genre.getId())) {
                        String sqlGenre = "insert into genrefilm (film_id, genre_id) " +
                                "values (?, ?)";
                        jdbcTemplate.update(sqlGenre,
                                film.getId(),
                                genre.getId());
                        ids.add(genre.getId());
                    }
                }

                String sqlDeleteLike = "delete from likes where film_id = ?";
                jdbcTemplate.update(sqlDeleteLike
                        , film.getLikes());

                if (film.getLikes() != null) {
                    for (Long like : film.getLikes()) {
                        String sqlLike = "insert into likes (film_id, user_id) " +
                                "values (?, ?)";
                        jdbcTemplate.update(sqlLike,
                                film.getId(),
                                like);
                    }
                }
            }
            return find(film.getId());
        } else return null;
    }

    @Override
    public void delete(Film film) {

        String sqlDeleteGenre = "delete from genrefilm where film_id = ?";
        jdbcTemplate.update(sqlDeleteGenre
                , film.getId());


        String sqlDeleteLike = "delete from likes where film_id = ?";
        jdbcTemplate.update(sqlDeleteLike
                , film.getLikes());


        String sqlDeleteFilm = "delete from film where id = ?";
        jdbcTemplate.update(sqlDeleteFilm
                , film.getId());
    }

    @Override
    public Film find(long id) {

        String sqlFilm = "select f.name as name, " +
                "f.description as description, " +
                "f.releaseDate as releaseDate, " +
                "f.duration as duration, " +
                "r.id as MPAid, " +
                "r.name as MPAname " +
                "from Film as f " +
                "inner join Ratings as r on r.id = f.mpa " +
                "where f.id = ?";
        Film film = jdbcTemplate.queryForObject(sqlFilm, new Object[]{id}, (rs, rowNum) ->
                new Film(
                        id,
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("releaseDate").toLocalDate(),
                        rs.getLong("duration"),
                        new MPA(rs.getLong("MPAid"), rs.getString("MPAname")),
                        getGenresUtil(id),
                        getLikes(id)
                ));

        return film;
    }

    @Override
    public List<Film> findAll() {
        String sqlFilms = "select f.id as id, " +
                "f.name as name, " +
                "f.description as description, " +
                "f.releaseDate as releaseDate, " +
                "f.duration as duration, " +
                "r.id as MPAid, " +
                "r.name as MPAname " +
                "from Film as f " +
                "inner join Ratings as r on r.id = f.mpa ";
        List<Film> films = jdbcTemplate.query(sqlFilms, (rs, rowNum) ->
                new Film(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("releaseDate").toLocalDate(),
                        rs.getLong("duration"),
                        new MPA(rs.getLong("MPAid"), rs.getString("MPAname")),
                        getGenresUtil(rs.getLong("id")),
                        getLikes(rs.getLong("id"))
                ));
        return films;
    }

    @Override
    public Film addLike(long id, long userId) {
        String sqlLike = "insert into likes (film_id, user_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlLike,
                id,
                userId);
        return find(id);
    }

    @Override
    public Film removeLike(long id, long userId) {
        String sqlDeleteLike = "delete from likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlDeleteLike
                ,id
                ,userId);
        return find(id);
    }

    @Override
    public List<Film> getLikesAmount(int count) {
        String sqlQuery = "SELECT f.ID as id, f.NAME as name, f.DESCRIPTION as description, " +
                "f.RELEASEDATE as releaseDate, f.DURATION as duration, " +
                "r.id as MPAid, r.name as MPAname " +
                "FROM FILM AS f " +
                "LEFT JOIN LIKES as l ON l.FILM_ID = f.ID " +
                "INNER JOIN RATINGS as r on r.ID = f.MPA " +
                "GROUP BY f.ID ORDER BY COUNT(l.FILM_ID) DESC LIMIT ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, (rs, rowNum) ->
                new Film(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("releaseDate").toLocalDate(),
                        rs.getLong("duration"),
                        new MPA(rs.getLong("MPAid"), rs.getString("MPAname")),
                        getGenresUtil(rs.getLong("id")),
                        getLikes(rs.getLong("id"))
                ), count);
        return films;
    }

    @Override
    public boolean isExist(long id) {
        String sqlFilm = "select count(id) " +
                "from Film " +
                "where id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlFilm, new Object[]{id}, Integer.class);
        return (count == 1);
    }

    @Override
    public boolean containsUserLike(long id, long userId) {
        String sqlFilm = "select count(id) " +
                "from likes " +
                "where film_id = ? and user_id = ? ";
        Integer count = jdbcTemplate.queryForObject(sqlFilm, new Object[]{id, userId}, Integer.class);
        return (count == 1);
    }

    private List<Genre> getGenresUtil(Long id) {
        String sqlGenres = "select g.id as id, g.name as name from genre as g " +
                "inner join genreFilm as gf on gf.genre_id = g.id " +
                "where gf.film_id = ? ORDER BY g.id";
        List<Genre> genres = jdbcTemplate.query(sqlGenres, (rs, rowNum) ->
                new Genre(rs.getLong("id"), rs.getString("name")), id);
        return genres;
    }

    private Set<Long> getLikes(Long id) {
        String sqlLikes = "select user_Id from Likes where film_Id = ?";
        List<Long> likes = jdbcTemplate.query(sqlLikes, (rs, rowNum) -> rs.getLong("user_Id"), id);
        return new HashSet<>(likes);
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


