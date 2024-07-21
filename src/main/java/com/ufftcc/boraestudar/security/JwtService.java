package com.ufftcc.boraestudar.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);


    @Value("${security.jwt.secret}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private Long expirationTime;

    @Value("${security.jwt.jwtCookieName}")
    private String jwtCookieName;

    public JwtService() {
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookieName);
        return cookie != null ? cookie.getValue() : null;
    }

    public ResponseCookie generateJwtHttpOnlyCookie(String username) {
        String jwt = generateTokenFromUsername(username);
        return ResponseCookie.from(jwtCookieName, jwt)
            .httpOnly(true)
            .path("/")
            .maxAge(expirationTime)
            .build();
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookieName)
            .httpOnly(true)
            .path("/")
            .maxAge(0)
            .build();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public boolean isValidToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(Date.from(LocalDateTime.now().plusMinutes(expirationTime).atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(getKey(), SignatureAlgorithm.HS256)
            .compact();
    }
}
