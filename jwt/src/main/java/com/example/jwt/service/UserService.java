package com.example.jwt.service;

import com.example.jwt.entity.User;
import com.example.jwt.exception.UserNotFoundException;

public interface UserService {

    public void saveUser(User user);
    public User getUserByNameAndPassword(String name, String password) throws UserNotFoundException;

}
