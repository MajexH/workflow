package xyz.majexh.workflow.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;
import xyz.majexh.workflow.controller.ResEntity;
import xyz.majexh.workflow.exceptions.ExceptionEnum;
import xyz.majexh.workflow.exceptions.TokenException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 添加在usernameandpasswordFilter之前 用于填充securityContext
 */
@Slf4j
public class JWTFilter extends GenericFilterBean {

    private TokenAuthenticationManager manager;
    private List<RequestMatcher> doNotCheckMatcherChain;
    private static final String HEADER = "Authorization";

    public JWTFilter(TokenAuthenticationManager manager, String... doNotMatchUrls) {
        this.manager = manager;
        List<RequestMatcher> chain = new LinkedList<>();
        for (String url : doNotMatchUrls) {
            chain.add(new AntPathRequestMatcher(url));
        }
        this.setDoNotCheckMatcher(chain);
    }

    public void setDoNotCheckMatcher(List<RequestMatcher> doNotCheckMatcher) {
        this.doNotCheckMatcherChain = doNotCheckMatcher;
    }

    private boolean doNotCheck(HttpServletRequest request) {
        for (RequestMatcher matcher : this.doNotCheckMatcherChain) {
            if (matcher.matches(request)) {
                return true;
            }
        }
        return false;
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        UserDetails user = manager.attemptAuthenticateToken(request.getHeader(HEADER));
        if (user == null) {
            throw new TokenException(ExceptionEnum.TOKEN_WRONG.getMessage());
        }
        return new UsernamePasswordAuthenticationToken(user.getUsername(), "", user.getAuthorities());
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info(failed.getMessage());
        // 清理工作
        SecurityContextHolder.clearContext();

        response.setHeader("Content-Type", "application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setCharacterEncoding("utf-8");

        ResEntity.Entity<Object> entity = new ResEntity.Entity<>();

        if (failed instanceof TokenException) {
            TokenException temp = (TokenException) failed;
            response.setStatus(temp.getStatus().value());
            entity.setStatus(temp.getStatus());
        } else {
            entity.setStatus(HttpStatus.UNAUTHORIZED);
        }
        entity.setMessage(failed.getMessage());
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().print(mapper.writeValueAsString(entity));
        response.getWriter().flush();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (this.doNotCheck(request)) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        Authentication token = null;
        try {
            token = attemptAuthentication(request, response);
        } catch (AuthenticationException exception) {
            unsuccessfulAuthentication(request, response, exception);
            return;
        }
        if (token == null) {
            unsuccessfulAuthentication(request, response, new TokenException(ExceptionEnum.TOKEN_WRONG.getMessage()));
            return;
        }
        // 成功就设置Authentication
        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(servletRequest, servletResponse);
    }
}
