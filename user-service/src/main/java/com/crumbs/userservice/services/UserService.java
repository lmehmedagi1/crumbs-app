package com.crumbs.userservice.services;

import com.crumbs.userservice.exceptions.IncorrectPasswordException;
import com.crumbs.userservice.exceptions.UserAlreadyExistsException;
import com.crumbs.userservice.exceptions.UserNotFoundException;
import com.crumbs.userservice.models.User;
import com.crumbs.userservice.models.UserDetails;
import com.crumbs.userservice.repositories.UserDetailsRepository;
import com.crumbs.userservice.repositories.UserRepository;
import com.crumbs.userservice.requests.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Service
@Validated
public class UserService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserDetailsRepository userDetailsRepository) {
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
    }

    @Transactional(readOnly = true)
    public User getUserById(@NotNull UUID id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("Specified ID does not exists!"));
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(@NotBlank String email) {
        final User user = userRepository.findByEmail(email);
        if (user == null)
            throw new UserNotFoundException("Specified email does not exists!");

        return user;
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(@NotBlank String username) {
        final User user = userRepository.findByUsername(username);
        if (user == null)
            throw new UserNotFoundException("Specified username does not exists!");
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserByCredentials(@NotBlank String username, @NotBlank String password) {
        final User user = getUserByUsername(username);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new IncorrectPasswordException();
        return user;
    }

    public User registerUser(@NotNull @Valid RegisterRequest registerRequest) {

        if (userRepository.findByUsername(registerRequest.getUsername()) != null)
            throw new UserAlreadyExistsException("Username is taken, try another one!");
        else if (userRepository.findByEmail(registerRequest.getEmail()) != null)
            throw new UserAlreadyExistsException("Email is taken, try another one!");

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword((new BCryptPasswordEncoder(10)).encode(registerRequest.getPassword()));

        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(registerRequest.getFirst_name());
        userDetails.setLastName(registerRequest.getLast_name());
        userDetails.setPhoneNumber(registerRequest.getPhone_number());
        userDetails.setGender(registerRequest.getGender());

        user.setUserDetails(userDetails);
        userDetails.setUser(user);

        final User newUser = userRepository.save(user);
        userDetailsRepository.save(userDetails);

        return newUser;
    }

    @Transactional
    public void deleteUserById(@NotBlank String id) {
        if (!userRepository.existsById(UUID.fromString(id)))
            throw new UserNotFoundException("Specified ID does not exists!");

        userRepository.deleteById(UUID.fromString(id));
    }
}
