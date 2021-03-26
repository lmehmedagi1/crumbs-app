package com.crumbs.userservice.services;

import com.crumbs.userservice.exceptions.IncorrectPasswordException;
import com.crumbs.userservice.exceptions.UserAlreadyExistsException;
import com.crumbs.userservice.exceptions.UserNotFoundException;
import com.crumbs.userservice.models.User;
import com.crumbs.userservice.models.UserProfile;
import com.crumbs.userservice.repositories.UserProfileRepository;
import com.crumbs.userservice.repositories.UserRepository;
import com.crumbs.userservice.requests.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional(readOnly = true)
    public User getUserById(@NotNull UUID id) throws UserNotFoundException {
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

    public User registerUser(@NotNull RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()) != null)
            throw new UserAlreadyExistsException("Username is taken, try another one!");
        else if (userRepository.findByEmail(registerRequest.getEmail()) != null)
            throw new UserAlreadyExistsException("Email is taken, try another one!");

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword((new BCryptPasswordEncoder()).encode(registerRequest.getPassword()));

        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName(registerRequest.getFirst_name());
        userProfile.setLastName(registerRequest.getLast_name());
        userProfile.setPhoneNumber(registerRequest.getPhone_number());
        userProfile.setGender(registerRequest.getGender());

        user.setUserProfile(userProfile);
        userProfile.setUser(user);

        final User newUser = userRepository.save(user);
        userProfileRepository.save(userProfile);

        return newUser;
    }

    @Transactional
    public void deleteUserById(@NotBlank String id) {
        if (!userRepository.existsById(UUID.fromString(id)))
            throw new UserNotFoundException("Specified ID does not exists!");

        userRepository.deleteById(UUID.fromString(id));
    }
}
