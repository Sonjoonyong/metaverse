package com.example.metaverse.service;

import com.example.metaverse.dao.UserDAO;
import com.example.metaverse.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDAO userDAO;


    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User getUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public User saveUser(User user) {
        userDAO.saveUser(user);
        return user;
    }


}
