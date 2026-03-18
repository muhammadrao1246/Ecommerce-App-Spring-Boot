package com.muhammadrao1246.SpringDemoApp.utils;

import com.muhammadrao1246.SpringDemoApp.models.DTO.UserJwtTokenDto;
import com.muhammadrao1246.SpringDemoApp.models.User;
import com.muhammadrao1246.SpringDemoApp.models.types.AppAuthProviders;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
// a utitly bean which is going to provide token related functionality
// which includes generating and verifiying the token of JWT
public class TokenUtils {

    // injecting the secret key secretly
    @Value("${app_secret_key}")
    private String secretKey;

    // first we going to build secret key
    public SecretKey buildSecretKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public UserJwtTokenDto generateJWTTokens(User user){
        // going to generate token using io.jsonwebtokens library implementation
        String access_token = Jwts.builder()
                .subject(user.getUsername())
                .claim("email", user.getEmail())
                .id(user.getId().toString())
                .issuedAt(new Date(Instant.now().toEpochMilli()))
                .expiration(new Date(Instant.now().toEpochMilli() + 1000 * 60 * 60 * 24)) // for 1 day
                .signWith(buildSecretKey())
                .compact();

        // random refresh token logic here
        String refresh_token = "";

        return UserJwtTokenDto.builder()
                .access_token(access_token)
                .refresh_token(refresh_token)
                .build();
    }

    // Parse the JWT Token and get User Id else null
    public UUID getUserIdFromToken(String token){
        token = token.replace("Bearer ", "");

        // verifying payload
        Claims claims = Jwts.parser()
                .verifyWith(buildSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getId() != null ? UUID.fromString(claims.getId()) : null;
    }
}
