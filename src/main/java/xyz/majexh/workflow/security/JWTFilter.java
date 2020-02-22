package xyz.majexh.workflow.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import xyz.majexh.workflow.controller.ResEntity;
import xyz.majexh.workflow.exceptions.ExceptionEnum;
import xyz.majexh.workflow.exceptions.TokenException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 添加在usernameandpasswordFilter之前 用于填充securityContext
 */
@Slf4j
public class JWTFilter extends GenericFilterBean {

    private TokenAuthenticationManager manager;
    private RequestMatcher doNotCheckMatcher;

    public JWTFilter(String doNotMatchUrls, TokenAuthenticationManager manager) {
        this.manager = manager;
        this.setDoNotCheckMatcher(new AntPathRequestMatcher(doNotMatchUrls));
    }

    public void setDoNotCheckMatcher(RequestMatcher doNotCheckMatcher) {
        this.doNotCheckMatcher = doNotCheckMatcher;
    }

    private boolean doNotCheck(HttpServletRequest request) {
        return this.doNotCheckMatcher.matches(request);
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        UserDetails user = manager.attemptAuthenticateToken(request.getHeader("Authorization"));
        if (user == null) {
            throw new TokenException(ExceptionEnum.TOKEN_WRONG.getMessage());
        }
        return new UsernamePasswordAuthenticationToken(user.getUsername(), "", user.getAuthorities());
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info(failed.getMessage());
        response.setHeader("Content-Type", "application/json");
        response.setCharacterEncoding("utf-8");
        ResEntity res = new ResEntity();
        res.error(401, failed.getMessage());
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().print(mapper.writeValueAsString(res));
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
