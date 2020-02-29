package xyz.majexh.workflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.majexh.workflow.domain.User;
import xyz.majexh.workflow.security.TokenAuthenticationManager;
import xyz.majexh.workflow.service.UserService;
import xyz.majexh.workflow.utils.JwtUtils;

import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {

    private JwtUtils jwtUtils;
    private TokenAuthenticationManager authenticationManager;
    private UserService userService;

    @Autowired
    public void setAuthenticationManager(TokenAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    // required false 是因为之后进行了检查
    @PostMapping(path = "/login")
    public ResponseEntity<HashMap<String, Object>> login(@RequestBody(required = false) User user) throws Exception {
        this.authenticationManager.attemptAuthenticate(user);
        // 获取用户信息
        User info = this.userService.findUserByUsername(user.getUsername());
        HashMap<String, Object> res = new HashMap<>(){{
            this.put("token", jwtUtils.generateTokenWithoutPayloads(info));
        }};
        return ResEntity.okDefault(res);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<HashMap<String, Object>> register(@RequestBody(required = false) User user) throws Exception {
        this.userService.insertUser(user);
        return ResEntity.okDefault(null);
    }
}
