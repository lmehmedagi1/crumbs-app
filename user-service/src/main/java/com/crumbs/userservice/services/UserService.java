package com.crumbs.userservice.services;

import com.crumbs.userservice.exceptions.IncorrectPasswordException;
import com.crumbs.userservice.exceptions.UserAlreadyExistsException;
import com.crumbs.userservice.exceptions.UserNotFoundException;
import com.crumbs.userservice.models.User;
import com.crumbs.userservice.repositories.UserDetailsRepository;
import com.crumbs.userservice.repositories.UserRepository;
import com.crumbs.userservice.requests.RegisterRequest;
import com.crumbs.userservice.utilities.JWTUtil;
import com.crumbs.userservice.utilities.UserDetailsServiceImpl;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(@NonNull String email) {
        final User user = userRepository.findByEmail(email);
        if (user == null) throw new UserNotFoundException("User with this email does not exist");
        // Do not return password
        user.setPassword(null);
        return user;
    }

    @Transactional(readOnly = true)
    public User getUser(@NonNull String username, @NonNull String password) {
        final User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User with this username does not exist");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new IncorrectPasswordException("Password is incorrect");

        return user;
    }

    @Transactional(readOnly = true)
    public User getUser(@NonNull String username) {
        final User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User with this username does not exist");
        }
        return user;
    }

    @Transactional(readOnly = true)
    public String generateToken(@NonNull String username) {
        final UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
        return JWTUtil.generateToken(userDetails);
    }

    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()) != null)
            throw new UserAlreadyExistsException("User with this username already exists");

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword((new BCryptPasswordEncoder()).encode(registerRequest.getPassword()));

        com.crumbs.userservice.models.UserDetails userDetails = new com.crumbs.userservice.models.UserDetails();
        userDetails.setFirstName(registerRequest.getFirstName());
        userDetails.setLastName(registerRequest.getLastName());
        userDetails.setPhoneNumber(registerRequest.getPhoneNumber());
        userDetails.setGender(registerRequest.getGender());

        user.setUserDetails(userDetails);
        userDetails.setUser(user);

        final User newUser = userRepository.save(user);
        userDetailsRepository.save(userDetails);

        return newUser;
    }

    @Transactional
    public void deleteUser(@NonNull String id) {
        if (!userRepository.existsById(UUID.fromString(id)))
            throw new UserNotFoundException("The specified user does not exist :(");

        userRepository.deleteById(UUID.fromString(id));
    }
}
