package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
class FilmorateApplicationTests {

	FilmController filmController;
	UserController userController;
	Film filmForTest;
	User userForTest;

	@BeforeEach
	public void beforeEach() {
		filmController = new FilmController();
		userController = new UserController();
		filmForTest = new Film(1, "name", " description", LocalDate.parse("1993-06-28"), 30);
		userForTest = new User(1, "mail@mail.ru", "login", "name", LocalDate.parse("1993-06-28"));
	}

    @Test
    public void createFilmTest() throws ValidationException {
	  assertEquals(filmForTest, filmController.create(filmForTest));
	  filmForTest.setName("");
	  Throwable thrown = assertThrows(ValidationException.class, () -> {
		  filmController.create(filmForTest);
	  });
	  assertEquals("Название не может быть пустым!", thrown.getMessage());
	  filmForTest.setName("name");

	  byte[] bytes = new byte[201];
	  Arrays.fill(bytes, (byte)'A');
	  filmForTest.setDescription(new String(bytes));
	  thrown = assertThrows(ValidationException.class, () -> {
		  filmController.create(filmForTest);
	  });
	  assertEquals("Максимальная длина описания — 200 символов!", thrown.getMessage());
	  filmForTest.setDescription("A");

	  filmForTest.setReleaseDate(LocalDate.parse("1895-12-27"));
	  thrown = assertThrows(ValidationException.class, () -> {
		  filmController.create(filmForTest);
	  });
	  assertEquals("Дата релиза — должна быть не раньше 28 декабря 1895 года!", thrown.getMessage());
      filmForTest.setReleaseDate(LocalDate.parse("1993-06-28"));

	  filmForTest.setDuration(-1);
	  thrown = assertThrows(ValidationException.class, () -> {
		  filmController.create(filmForTest);
	  });
	  assertEquals("Продолжительность фильма должна быть положительной!", thrown.getMessage());
	  filmForTest.setDuration(1);
	} // тестирование метода create (Film) и его исключений

	@Test
	public void putFilmTest() throws ValidationException {
		assertEquals(filmForTest, filmController.create(filmForTest));
		filmForTest.setName("");
		Throwable thrown = assertThrows(ValidationException.class, () -> {
			filmController.putFilm(filmForTest);
		});
		assertEquals("Название не может быть пустым!", thrown.getMessage());
		filmForTest.setName("name");

		byte[] bytes = new byte[201];
		Arrays.fill(bytes, (byte)'A');
		filmForTest.setDescription(new String(bytes));
		thrown = assertThrows(ValidationException.class, () -> {
			filmController.putFilm(filmForTest);
		});
		assertEquals("Максимальная длина описания — 200 символов!", thrown.getMessage());
		filmForTest.setDescription("A");

		filmForTest.setReleaseDate(LocalDate.parse("1895-12-27"));
		thrown = assertThrows(ValidationException.class, () -> {
			filmController.putFilm(filmForTest);
		});
		assertEquals("Дата релиза — должна быть не раньше 28 декабря 1895 года!", thrown.getMessage());
		filmForTest.setReleaseDate(LocalDate.parse("1993-06-28"));

		filmForTest.setDuration(-1);
		thrown = assertThrows(ValidationException.class, () -> {
			filmController.putFilm(filmForTest);
		});
		assertEquals("Продолжительность фильма должна быть положительной!", thrown.getMessage());
		filmForTest.setDuration(1);
	} //тестирование метода  putFilm и его исключений

	@Test
	public void findAllFilmsTest() throws ValidationException {
		assertTrue(filmController.findAllFilms().isEmpty());
		filmController.create(filmForTest);
		assertFalse(filmController.findAllFilms().isEmpty());
		assertEquals(1,filmController.findAllFilms().size());
	    assertEquals(filmForTest, filmController.findAllFilms().get(0));

	} //тестирование метода findAllFilms

	@Test
	public void createUserTest() throws ValidationException{
		assertEquals(userForTest, userController.create(userForTest));

		userForTest.setEmail("");
		Throwable thrown = assertThrows(ValidationException.class, () -> {
			userController.create(userForTest);
		});
		assertEquals("Электронная почта не может быть пустой и должна содержать символ @!", thrown.getMessage());
		userForTest.setEmail("mailmail.ru");
		thrown = assertThrows(ValidationException.class, () -> {
			userController.create(userForTest);
		});
		assertEquals("Электронная почта не может быть пустой и должна содержать символ @!", thrown.getMessage());
		userForTest.setEmail("mail@mail.ru");

		userForTest.setLogin("");
		thrown = assertThrows(ValidationException.class, () -> {
			userController.create(userForTest);
		});
		assertEquals("Логин не может быть пустым и содержать пробелы!", thrown.getMessage());
		userForTest.setLogin("lo gin");
		thrown = assertThrows(ValidationException.class, () -> {
			userController.create(userForTest);
		});
		assertEquals("Логин не может быть пустым и содержать пробелы!", thrown.getMessage());
		userForTest.setLogin("login");


		userForTest.setBirthday(LocalDate.parse("2022-07-01"));
		thrown = assertThrows(ValidationException.class, () -> {
			userController.create(userForTest);
		});
		assertEquals("Дата рождения не может быть в будущем.", thrown.getMessage());
		userForTest.setBirthday(LocalDate.parse("1993-06-28"));

		userForTest.setName("");
		thrown = assertThrows(ValidationException.class, () -> {
			userController.create(userForTest);
		});
		assertEquals("Имя для отображения не может быть пустым." +
				"В случае отсутствия имя будет использован логин!", thrown.getMessage());
		userForTest.setName("name");
	} // тестирование метода create (User) и его исключений

	@Test
	public void putUserTest() throws ValidationException{
		assertEquals(userForTest, userController.create(userForTest));

		userForTest.setEmail("");
		Throwable thrown = assertThrows(ValidationException.class, () -> {
			userController.putUser(userForTest);
		});
		assertEquals("Электронная почта не может быть пустой и должна содержать символ @!", thrown.getMessage());
		userForTest.setEmail("mailmail.ru");
		thrown = assertThrows(ValidationException.class, () -> {
			userController.putUser(userForTest);
		});
		assertEquals("Электронная почта не может быть пустой и должна содержать символ @!", thrown.getMessage());
		userForTest.setEmail("mail@mail.ru");

		userForTest.setLogin("");
		thrown = assertThrows(ValidationException.class, () -> {
			userController.putUser(userForTest);
		});
		assertEquals("Логин не может быть пустым и содержать пробелы!", thrown.getMessage());
		userForTest.setLogin("lo gin");
		thrown = assertThrows(ValidationException.class, () -> {
			userController.putUser(userForTest);
		});
		assertEquals("Логин не может быть пустым и содержать пробелы!", thrown.getMessage());
		userForTest.setLogin("login");

		userForTest.setBirthday(LocalDate.parse("2022-07-01"));
		thrown = assertThrows(ValidationException.class, () -> {
			userController.putUser(userForTest);
		});
		assertEquals("Дата рождения не может быть в будущем.", thrown.getMessage());
		userForTest.setBirthday(LocalDate.parse("1993-06-28"));

		userForTest.setName("");
		thrown = assertThrows(ValidationException.class, () -> {
			userController.putUser(userForTest);
		});
		assertEquals("Имя для отображения не может быть пустым." +
				"В случае отсутствия имя будет использован логин!", thrown.getMessage());
		userForTest.setName("name");
	} //тестирование метода  putUser и его исключений

	@Test
	public void findAllUserTest() throws ValidationException {
		assertTrue(userController.findAllUser().isEmpty());
		userController.create(userForTest);
		assertFalse(userController.findAllUser().isEmpty());
		assertEquals(1,userController.findAllUser().size());
		assertEquals(userForTest, userController.findAllUser().get(0));
	} //тестирование метода findAllUser и его исключений
}
