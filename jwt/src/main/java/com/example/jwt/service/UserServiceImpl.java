package com.example.jwt.service;


import com.example.jwt.entity.User;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserByNameAndPassword(String name, String password) throws UserNotFoundException {
        User user = userRepository.findByUsernameAndPassword(name, password);
        if(user == null){
            throw new UserNotFoundException("Invalid id and password");
        }


        return user;
    }
}
