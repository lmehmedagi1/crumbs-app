package com.crumbs.userservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public final class JwtConfigAndUtil {

    private String secretKey = "e94a08b7f23735dcc5a14af50ccb5ba1617d0c3f299c0b133cc1c7a3c2474342a54cb2179207868bcadb1ce812f9fc757bfcc243d153fe9740a345a1e167b6f0";
    @Value("${application.jwt.token-prefix}")
    private String tokenPrefix;
    @Value("${application.jwt.token-expiration-after-days}")
    private Integer tokenExpiration;

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public Integer getTokenExpiration() {
        return tokenExpiration;
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(@NotBlank String token) {
        return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(@NotNull String id) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, id);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(getTokenExpiration())))
                .signWith(getSecretKey()).compact();
    }
}
