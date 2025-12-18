package org.commitlink.procure.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.commitlink.procure.models.AuthUser;
import org.commitlink.procure.services.IJwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService implements IJwtService {
    @Value("${security.jwt.secret-key}")
    @Getter
    private String jwtSecretKey;
    @Getter
    @Value("${security.jwt.expiration}")
    private Long expiration;
    @Getter
    @Value("${security.jwt.refresh-token.expiration}")
    private Long refreshTokenExpiration;

    @Override
    public String generateToken(AuthUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        claims.put("authorities", user.getAuthorities());
        return buildJwtToken(claims, user, this.expiration);
    }

    @Override
    public String generateRefreshToken(AuthUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        claims.put("authorities", user.getPermissions());
        return buildJwtToken(claims, user, this.refreshTokenExpiration);
    }


    public boolean isTokenValid(String token, AuthUser user) throws JwtException {
        String username = getUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }


    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) throws JwtException {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);

    }

    private Claims extractAllClaims(String token) throws JwtException {
        return Jwts.parser()
                .setSigningKey(getSignedKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Key getSignedKey() {
        byte[] decodedKey = Base64.getDecoder().decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public String buildJwtToken(
            Map<String, Object> claims,
            AuthUser user,
            Long expiration
    ) {

        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuer(user.getFirstName().concat(" ").concat(user.getLastName()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignedKey())
                .compact();
    }


    private boolean isTokenExpired(String token) throws JwtException {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

    public String getUsername(String token) throws JwtException {
        return extractClaims(token, Claims::getSubject);
    }

}
