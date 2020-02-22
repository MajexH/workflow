package xyz.majexh.workflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.majexh.workflow.domain.User;
import xyz.majexh.workflow.exceptions.BaseException;
import xyz.majexh.workflow.exceptions.ExceptionEnum;
import xyz.majexh.workflow.security.TokenAuthenticationManager;
import xyz.majexh.workflow.utils.JwtUtils;
import xyz.majexh.workflow.utils.StringUtils;

import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {

    private JwtUtils jwtUtils;
    private TokenAuthenticationManager authenticationManager;

    @Autowired
    public void setAuthenticationManager(TokenAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    // required false 是因为之后进行了检查
    @PostMapping(path = "/login")
    public ResEntity<HashMap<String, Object>> login(@RequestBody(required = false) User user) throws Exception {
        System.out.println(user);
        this.authenticationManager.attemptAuthenticate(user);
        HashMap<String, Object> res = new HashMap<>(){{
            this.put("token", jwtUtils.generateTokenWithoutPayloads(user));
        }};
        return new ResEntity<HashMap<String, Object>>().okDefault(res);
    }
}
