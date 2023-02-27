package com.example.jwt.config;

import com.example.jwt.entity.User;

import java.util.Map;

public interface TokenGenerator {
    Map<String, String> generateToken(User user);
}
