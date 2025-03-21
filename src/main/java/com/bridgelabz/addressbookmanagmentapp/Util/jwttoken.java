package com.bridgelabz.addressbookmanagmentapp.Util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class jwttoken {

    private static final String TOKEN_SECRET = "Lock";
    private static final Map<Long, String> activeTokens = new HashMap<>();

    public String createToken(Long id) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);

            String token = JWT.create()
                    .withClaim("user_id", id)
                    .withExpiresAt(Date.from(Instant.now().plusSeconds(60))) // Token valid for 1 minute
                    .sign(algorithm);

            activeTokens.put(id, token); // Save token
            return token;

        } catch (JWTCreationException | IllegalArgumentException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Long decodeToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getClaim("user_id").asLong();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Invalid or expired token.");
        }
    }

    public boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Token has expired or is invalid.");
        }
    }

    public boolean isUserLoggedIn(Long userId, String token) {
        return activeTokens.containsKey(userId) && activeTokens.get(userId).equals(token);
    }

    public void logoutUser(Long userId) {
        activeTokens.remove(userId);
    }

    public Long getCurrentUserId(String token) {
        return decodeToken(token);
    }

    public String getCurrentToken(Long userId) {
        return activeTokens.get(userId);
    }
}
