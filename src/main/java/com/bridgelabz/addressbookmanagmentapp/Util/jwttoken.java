package com.bridgelabz.addressbookmanagmentapp.Util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class jwttoken{

    @Autowired
    static String TOKEN_SECRET = "Lock";
    @Autowired
    static Map<Long, String> activeTokens = new HashMap<>();

    public String createToken(Long id)   {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);

            String token = JWT.create()
                    .withClaim("user_id", id)
                    .sign(algorithm);
            activeTokens.put(id, token);
            return token;

        } catch (JWTCreationException exception) {
            exception.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
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

    public boolean isUserLoggedIn(Long userId, String token) {
        return activeTokens.containsKey(userId) && activeTokens.get(userId).equals(token);
    }

    public void logoutUser(Long userId) {
        activeTokens.remove(userId);
    }


    public Long getCurrentUserId(String token) {
        return decodeToken(token); // Extracts the user ID from the token
    }

    public String getCurrentToken(Long userId) {
        return activeTokens.get(userId);
    }


}