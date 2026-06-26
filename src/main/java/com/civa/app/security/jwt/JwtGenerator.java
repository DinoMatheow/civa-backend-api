package com.civa.app.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtGenerator {
    
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private long jwtExperitaion;

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date experiteDate = new Date(currentDate.getTime() + jwtExperitaion);

        String token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(experiteDate)
                .signWith(getSigningKey(), Jwts.SIG.HS512)
                .compact();

        return token;


        
    }


}
