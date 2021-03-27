package com.crumbs.userservice.jwt;

import com.crumbs.userservice.requests.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtConfigAndUtil jwtConfigAndUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtConfigAndUtil jwtConfigAndUtil, HandlerExceptionResolver handlerExceptionResolver) {
        this.setFilterProcessesUrl("/account/login");
        this.authenticationManager = authenticationManager;
        this.jwtConfigAndUtil = jwtConfigAndUtil;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest loginRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequest.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            );
            return authenticationManager.authenticate(authentication);
        } catch (IOException exception) {
            handlerExceptionResolver.resolveException(request, response, null, new IllegalStateException());
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, new BadCredentialsException("Bad credentials"));
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfigAndUtil.getTokenExpiration())))
                .signWith(jwtConfigAndUtil.getSecretKey())
                .compact();
        response.addHeader(jwtConfigAndUtil.getAuthorizationHeader(), jwtConfigAndUtil.getTokenPrefix() + token);
    }
}
