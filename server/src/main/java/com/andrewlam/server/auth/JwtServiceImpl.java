package com.andrewlam.server.auth;

import com.andrewlam.server.config.JwtProperties;
import com.andrewlam.server.enums.UserRole;
import com.andrewlam.server.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String ROLE_CLAIM = "role";
    private static final String USER_ID_CLAIM = "userId";

    private final JwtProperties jwtProperties;

    public JwtServiceImpl(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    // Creates a signed JWT token containing
    // (subject, issuer, issuedAt, expiration, custom claim userId, custom claim role)
    @Override
    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getAccessTokenExpirationMs());

        return Jwts.builder()
                .subject(normalizeEmail(user.getEmail()))
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiration(expiryDate)
                .claim(USER_ID_CLAIM, user.getId())
                .claim(ROLE_CLAIM, user.getRole().name())
                .signWith(getSigningKey())
                .compact();
    }

    // Reads the email back from the token subject
    @Override
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Reads the userId claim back from the token
    @Override
    public Long extractUserId(String token) {
        Object userIdClaim = extractAllClaims(token).get(USER_ID_CLAIM);

        if (userIdClaim instanceof Integer integerValue) {
            return integerValue.longValue();
        }

        if (userIdClaim instanceof Long longValue) {
            return longValue;
        }

        if (userIdClaim instanceof Number numberValue) {
            return numberValue.longValue();
        }

        throw new JwtException("Invalid userId claim in token.");
    }

    // Reads the role claim back from the token and converts it to enum
    @Override
    public UserRole extractRole(String token) {
        String roleValue = extractAllClaims(token).get(ROLE_CLAIM, String.class);
        return UserRole.valueOf(roleValue);
    }

    // check if
    // token can be parsed
    // signature is valid
    // token is not expired
    // email inside token matches expected user email
    @Override
    public boolean isTokenValid(String token, String expectedEmail) {
        try {
            String tokenEmail = extractEmail(token);

            return Objects.equals(normalizeEmail(expectedEmail), normalizeEmail(tokenEmail))
                    && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}