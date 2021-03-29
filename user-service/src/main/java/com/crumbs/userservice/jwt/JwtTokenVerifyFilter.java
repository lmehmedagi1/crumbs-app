package com.crumbs.userservice.jwt;

import com.crumbs.userservice.exceptions.UserNotFoundException;
import com.crumbs.userservice.services.CustomUserDetailsService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenVerifyFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtConfigAndUtil jwtConfigAndUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    public JwtTokenVerifyFilter(CustomUserDetailsService customUserDetailsService, JwtConfigAndUtil jwtConfigAndUtil, HandlerExceptionResolver handlerExceptionResolver) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtConfigAndUtil = jwtConfigAndUtil;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader(jwtConfigAndUtil.getAuthorizationHeader());

        String username = null;
        String jwt = null;

        try {
            if (authorizationHeader != null && authorizationHeader.startsWith(jwtConfigAndUtil.getTokenPrefix())) {
                jwt = authorizationHeader.substring(7);
                username = jwtConfigAndUtil.extractUsername(jwt);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                final UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
                if (jwtConfigAndUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken upAuthToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    upAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(upAuthToken);
                }
            }
        } catch (JwtException | UserNotFoundException exception) {
            handlerExceptionResolver.resolveException(request, response, null, new JwtException("Authorization failed"));
            return;
        }

        chain.doFilter(request, response);
    }
}
