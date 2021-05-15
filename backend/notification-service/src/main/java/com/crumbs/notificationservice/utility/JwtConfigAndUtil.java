package com.crumbs.notificationservice.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.function.Function;

@Component
public final class JwtConfigAndUtil {

    private String secretKey = "e94a08b7f23735dcc5a14af50ccb5ba1617d0c3f299c0b133cc1c7a3c2474342a54cb2179207868bcadb1ce812f9fc757bfcc243d153fe9740a345a1e167b6f0";

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(@NotBlank String token) {
        return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();
    }
}