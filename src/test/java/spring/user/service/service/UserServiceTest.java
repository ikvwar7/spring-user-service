package spring.user.service.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import spring.user.service.domain.User;
import spring.user.service.exceptions.UserNotFoundException;

import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    private User savedUser = new User(1L, "Petr", "Petrov", LocalDate.of(2015, 02,
            02), "same@m.ru", "123");

    @Autowired
    private UserService userService;

    @Test(expected = RuntimeException.class)
    public void whenUserWithEmailExistsThrowException() {
        userService.save(savedUser);
        userService.save(savedUser);
    }

    @Test(expected = UserNotFoundException.class)
    public void whenUserNotFoundByEmailThrowException() {
        userService.findByEmail("notExisted@m.ru");
    }

    @Test(expected = UserNotFoundException.class)
    public void whenUserNotFoundByIdThrowException() {
        userService.delete(2L);
    }

    @Test
    public void whenUserEmailCorrectReturnOk() {
        userService.save(savedUser);
        User user = userService.findByEmail("same@m.ru");

        assertEquals(savedUser, user);
    }
}