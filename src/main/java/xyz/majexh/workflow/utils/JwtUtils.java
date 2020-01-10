package xyz.majexh.workflow.utils;

import io.jsonwebtoken.Jwts;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
@Data
public class JwtUtils {

    @Value("${jwt.expirationTime}")
    private long expireTime;

    @Value("${jwt.secret}")
    private String secret;



//    public String generateToken(UserDetails userDetails, Map<String, Object> payloads) {
//        return Jwts.builder()
//                .setClaims(payloads)
//                .setExpiration(new Date(Instant.now().toEpochMilli() + expireTime))
//                .signWith()
//                .compact();
//    }
//
//    public Boolean validateToken(String token, UserDetails userDetails) {
//        String username = userDetails.getUsername();
//        return (username.equals())
//    }
//
//    public String getUsernameFromToken(String token) {
//
//    }
}
