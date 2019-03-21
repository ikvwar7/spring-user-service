package spring.user.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.user.service.dao.UserDao;
import spring.user.service.domain.User;

@Component
public class UserService {

    @Autowired
    private UserDao userDao;

    public void save(User user) {
        userDao.save(user);
    }

    public void delete(Long id) {
        userDao.delete(id);
    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }
}