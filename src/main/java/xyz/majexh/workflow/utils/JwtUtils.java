package xyz.majexh.workflow.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import xyz.majexh.workflow.domain.User;
import xyz.majexh.workflow.exceptions.BaseException;
import xyz.majexh.workflow.exceptions.ExceptionEnum;
import xyz.majexh.workflow.service.UserService;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class JwtUtils {

    @Value("${jwt.expirationTime}")
    private long expireTime;

    @Value("${jwt.secret}")
    private String secret;

    private UserService userService;
    private static SignatureAlgorithm algorithm = SignatureAlgorithm.HS512;
    private static final String TOKEN_PREFIX = "Bearer";

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String generateToken(User userDetails, Map<String, Object> payloads) {
        payloads.put("userId", userDetails.getId());
        payloads.put("username", userDetails.getUsername());
        payloads.put("name", userDetails.getName());
        return Jwts.builder()
                .setClaims(payloads)
                .setExpiration(new Date(Instant.now().toEpochMilli() + expireTime))
                .signWith(algorithm, secret)
                .compact();
    }

    /**
     * 默认携带userId username name
     * @param userDetails
     * @return
     */
    public String generateTokenWithoutPayloads(User userDetails) {
        return this.generateToken(userDetails, new HashMap<>());
    }

    /**
     * validate token
     * @param token
     * @return
     */
    public UserDetails validateToken(String token) throws BaseException {
        Claims claims = Jwts.parser()
                            .setSigningKey(secret)
                            // 去掉 Bearer
                            .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                            .getBody();
        // 超时
        if (claims.getExpiration().getTime() < Instant.now().toEpochMilli()) {
            throw new BaseException(ExceptionEnum.TOKEN_EXPIRE);
        }
        UserDetails user = userService.loadUserByUsername((String) claims.get("username"));
        // 找不到这个username的用户
        if (user == null) {
            throw new BaseException(ExceptionEnum.TOKEN_WRONG);
        }
        // 其余验证token成功
        return user;
    }

}
