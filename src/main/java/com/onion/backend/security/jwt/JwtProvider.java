package com.onion.backend.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JwtProvider {

    private final String secretkey;
    private final long expiration;
    private final Key key;

    public JwtProvider(
        @Value("${jwt.security.key}")
        String secretkey,
        @Value("${jwt.security.expiration}")
        Long expiration) {
        this.secretkey = secretkey;
        this.expiration = expiration;
        this.key = Keys.hmacShaKeyFor(secretkey.getBytes());
    }


    public String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public String getUsername(String token) {
        return getClaims(token)
            .getSubject();
    }


    public boolean isValidToken(String token) {
        return !isExpired(token);
    }


    private boolean isExpired(String token) {
        return getClaims(token)
            .getExpiration()
            .before(new Date());
    }


    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (JwtException je) {
            log.error("JwtException : {}", je.getMessage());
            throw je;
        }
    }

    public LocalDateTime getExpiration(String token) {
        return getClaims(token)
            .getExpiration().toInstant()
            .atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}
