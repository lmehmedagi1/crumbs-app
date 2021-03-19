package com.crumbs.userservice.controllers;

import com.crumbs.userservice.exceptions.IncorrectPasswordException;
import com.crumbs.userservice.exceptions.UserAlreadyExistsException;
import com.crumbs.userservice.exceptions.UserNotFoundException;
import com.crumbs.userservice.models.User;
import com.crumbs.userservice.requests.LoginRequest;
import com.crumbs.userservice.requests.RegisterRequest;
import com.crumbs.userservice.responses.LoginAndRegistrationResponse;
import com.crumbs.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/users/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        try {
            return ResponseEntity.ok(userService.getUserByEmail(email));
        }
        catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginAndRegistrationResponse> login(@RequestBody @Valid LoginRequest loginRequest) {

        try {
            final User user = userService.getUser(loginRequest.getUsername(), loginRequest.getPassword());
            final String jwt = userService.generateToken(loginRequest.getUsername());
            return ResponseEntity.ok(new LoginAndRegistrationResponse(user, jwt));
        }
        catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        catch (IncorrectPasswordException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<LoginAndRegistrationResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {

        try {
            final User user = userService.registerUser(registerRequest);
            final String jwt = userService.generateToken(registerRequest.getUsername());
            return ResponseEntity.ok(new LoginAndRegistrationResponse(user, jwt));
        }
        catch (UserAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/users/delete")
    public void deleteUser(@RequestParam @Valid String id) {
        try {
            userService.deleteUser(id);
        } catch(UserNotFoundException recipeNotFoundException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, recipeNotFoundException.getMessage());
        }
    }
}
