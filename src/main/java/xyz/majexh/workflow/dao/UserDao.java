package xyz.majexh.workflow.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.majexh.workflow.domain.User;

import java.util.List;

public interface UserDao extends JpaRepository<User, Integer> {

    List<User> findAll();

    User findUserByUsernameEquals(String username);

}
