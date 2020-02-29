package xyz.majexh.workflow.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.majexh.workflow.dao.UserDao;
import xyz.majexh.workflow.domain.User;
import xyz.majexh.workflow.exceptions.BaseException;
import xyz.majexh.workflow.exceptions.ExceptionEnum;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private UserDao userDao;
    private PasswordEncoder encoder;

    @Autowired
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userDao.findUserByUsernameEquals(username);
    }

    public User findUserByUsername(String username) throws UsernameNotFoundException {
        return this.userDao.findUserByUsernameEquals(username);
    }

    public void insertUser(User user) throws BaseException {
        if (user == null || user.isNone())
            throw new BaseException(ExceptionEnum.INSUFFICIENT_PARAMS.getStatus(), "注册参数错误");
        if (this.userDao.findUserByUsernameEquals(user.getUsername()) != null) {
            throw new BaseException(ExceptionEnum.USER_EXITS);
        }
        user.setPassword(encoder.encode(user.getPassword()));
        this.userDao.save(user);
    }
}
