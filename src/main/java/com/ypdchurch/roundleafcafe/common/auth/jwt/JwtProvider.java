package com.ypdchurch.roundleafcafe.common.auth.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Getter
@Configuration
public class JwtProvider {
    public static final String TOKEN_PREFIX = "Bearer "; // 스페이스 필요함
    public static final String REFRESH_TOKEN_HEADER = "refreshToken";
    private final String secretKey;
    private final int accessValidTime;
    private final int refreshValidTime;

    public JwtProvider(@Value("${custom.jwt.secretKey}") String secretKey
            , @Value("${custom.jwt.accessTokenTime}") int accessValidTime
            , @Value("${custom.jwt.refreshTokenTime}") int refreshValidTime) {
        this.secretKey = secretKey;
        this.accessValidTime = accessValidTime;
        this.refreshValidTime = refreshValidTime;
    }

    public String createAccessToken(String email, @Value("${custom.jwt.accessTokenTime}") int validTime) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .header()
                .type("JWT")
                .and()
                .subject(email)
                .expiration(new Date(System.currentTimeMillis() + validTime))//a java.util.Date
                .issuedAt(new Date(System.currentTimeMillis())) // for example, now
                .claim("email", email)
                .signWith(makeEncryptedSecretKey(secretKey))
                .compact();//just an example id
    }

    public String createRefreshToken(String email, @Value("${custom.jwt.refreshTokenTIme}") int validTime) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(email)
                .expiration(new Date(System.currentTimeMillis() + validTime))//a java.util.Date
                .issuedAt(new Date(System.currentTimeMillis())) // for example, now
                .claim("email", email)
                .signWith(makeEncryptedSecretKey(secretKey))
                .compact();//just an example id
    }

    public Jws<Claims> verify(String token) {
        try {
            Jws<Claims> claimsJws = getClaims(token);
            log.info("claimsJws == {}", claimsJws);
            return claimsJws;
        } catch (JwtException e) {
            throw new IllegalArgumentException();
        }
    }

    public Authentication getAuthentication(String token) {
        String email = this.findEmail(token);
        return new UsernamePasswordAuthenticationToken(email, token, null);
    }

    public String findEmail(String token) {
        Jws<Claims> claimsJws = getClaims(token);
        return claimsJws.getPayload()
                .get("email").toString();
    }

    private Jws<Claims> getClaims(String token) {
        return Jwts.parser()
                .verifyWith(makeEncryptedSecretKey(secretKey))
                .build()
                .parseSignedClaims(token);
    }

    private SecretKey makeEncryptedSecretKey(String secretKey) {
        String base64SecretKey = Base64.getEncoder()
                .encodeToString(secretKey.getBytes());
        return Keys.hmacShaKeyFor(base64SecretKey.getBytes());
    }

}

