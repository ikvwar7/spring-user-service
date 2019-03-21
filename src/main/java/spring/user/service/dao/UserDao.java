package spring.user.service.dao;

import spring.user.service.domain.User;

public interface UserDao {
    void save(User user);

    void delete(Long id);

    User findByEmail(String email);
}
