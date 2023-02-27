package com.example.jwt.config;

import com.example.jwt.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenGeneratorImpl implements TokenGenerator{

    @Value("${app.jwttoken.message}")
    String message;

    @Value("${jwt.secret}")
    String secret;

    @Override
    public Map<String, String> generateToken(User user) {
        Map<String,String> result = new HashMap<>();

        String token = Jwts.builder().setSubject(user.getUsername())
                        .setIssuedAt(new Date())
                                .signWith(SignatureAlgorithm.HS256,secret)
                                        .compact();

        result.put("token",token);
        result.put("message",message);
        return result;
    }
}
