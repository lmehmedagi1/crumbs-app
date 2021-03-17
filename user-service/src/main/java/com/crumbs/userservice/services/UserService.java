package com.crumbs.userservice.services;

import com.crumbs.userservice.exceptions.UserNotFoundException;
import com.crumbs.userservice.models.User;
import com.crumbs.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        final User user = userRepository.findByEmail(email);
        if (user == null) throw new UserNotFoundException("User with this email does not exist");
        // Do not return password
        user.setPassword(null);
        return user;
    }
}
