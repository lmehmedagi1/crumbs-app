package com.crumbs.gatewayservice.utility;

import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.crumbs.gatewayservice.utility.SecurityConstants.HEADER_STRING;
import static com.crumbs.gatewayservice.utility.SecurityConstants.TOKEN_PREFIX;
import static com.fasterxml.jackson.annotation.JsonFormat.DEFAULT_TIMEZONE;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader(HEADER_STRING);

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            try {
                jwt = authorizationHeader.substring(7);
                JWTUtil jwtUtil = new JWTUtil();
                username = jwtUtil.extractUsername(jwt);
            } catch (SignatureException | MalformedJwtException ignore) {
                handleException("Invalid JWT signature", request.getRequestURI(), response);
                return;
            } catch (ExpiredJwtException ignore) {
                handleException("JWT token has expired", request.getRequestURI(), response);
                return;
            } catch (RuntimeException ignore) {
                handleException("Unable to get JWT token", request.getRequestURI(), response);
                return;
            }
            if (username == null) {
                handleException("Invalid credentials", request.getRequestURI(), response);
                return;

            }
        }


        chain.doFilter(request, response);
    }

    private void handleException(String message, String path, HttpServletResponse response) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        formatter.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
        Date date = new Date(System.currentTimeMillis());
        ApiError apiError = new ApiError(formatter.format(date), HttpStatus.UNAUTHORIZED.value(), message, "Unauthorized", path);
        Gson gson = new Gson();
        response.resetBuffer();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setHeader("Content-Type", "application/json");
        response.getOutputStream().print(gson.toJson(apiError));
        response.flushBuffer();
    }
}