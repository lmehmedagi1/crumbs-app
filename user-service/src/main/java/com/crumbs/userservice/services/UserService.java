package com.crumbs.userservice.services;

import com.crumbs.userservice.models.User;
import com.crumbs.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String getUserEmail() {
        final User user = userRepository.findByFirstName("lejla");
        return user.getEmail();
    }
}
