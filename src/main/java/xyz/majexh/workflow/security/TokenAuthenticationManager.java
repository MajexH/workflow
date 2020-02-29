package xyz.majexh.workflow.security;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import xyz.majexh.workflow.domain.User;
import xyz.majexh.workflow.exceptions.BaseException;
import xyz.majexh.workflow.exceptions.ExceptionEnum;
import xyz.majexh.workflow.exceptions.TokenException;
import xyz.majexh.workflow.utils.JwtUtils;
import xyz.majexh.workflow.utils.StringUtils;

/**
 * 封装用户登录的认证逻辑
 */
@Component
public class TokenAuthenticationManager {

    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    public TokenAuthenticationManager(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public void attemptAuthenticate(User user) throws BaseException {
        if (user == null || StringUtils.checkNone(user.getUsername()) || StringUtils.checkNone(user.getPassword())) {
            throw new BaseException(ExceptionEnum.INSUFFICIENT_PARAMS.getStatus(), "账户名或密码缺失");
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername().trim(), user.getPassword().trim());
        try {
            authenticationManager.authenticate(authentication);
        } catch (AuthenticationException ex) {
            throw new BaseException(ExceptionEnum.WRONG_LOGIN);
        }
    }

    public UserDetails attemptAuthenticateToken(String token) {
        if (StringUtils.checkNone(token)) {
            throw new TokenException(ExceptionEnum.TOKEN_WRONG.getMessage(), ExceptionEnum.TOKEN_WRONG.getStatus());
        }
        UserDetails res;
        try {
            res = this.jwtUtils.validateToken(token);
        } catch (BaseException exception) {
            throw new TokenException(exception.getMessage());
        }
        return res;
    }
}
