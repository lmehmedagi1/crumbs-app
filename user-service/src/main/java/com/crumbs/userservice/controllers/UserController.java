package com.crumbs.userservice.controllers;

import com.crumbs.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/email")
    public String hello() {
        return userService.getUserEmail();
    }
}
