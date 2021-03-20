package com.crumbs.userservice.utility;

import com.crumbs.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        com.crumbs.userservice.models.User applicationUser = userService.getUser(username);

        return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
    }
}
