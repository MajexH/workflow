package xyz.majexh.workflow.dao;

import org.springframework.data.repository.Repository;
import xyz.majexh.workflow.domain.User;

import java.util.List;

public interface UserDao extends Repository<User, Integer> {

    List<User> findAll();

    User findUserByUsernameEquals(String username);
}
