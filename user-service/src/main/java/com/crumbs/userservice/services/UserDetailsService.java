package com.crumbs.userservice.services;

import com.crumbs.userservice.exceptions.UserNotFoundException;
import com.crumbs.userservice.models.UserDetails;
import com.crumbs.userservice.repositories.UserDetailsRepository;
import com.crumbs.userservice.requests.UserDetailsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Service
@Validated
public class UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;
    private final UserService userService;

    @Autowired
    public UserDetailsService(UserDetailsRepository userDetailsRepository, UserService userService) {
        this.userDetailsRepository = userDetailsRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public UserDetails getUserDetails(@NotNull UUID id) {
        return userDetailsRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    private void modifyUserDetails(UserDetailsRequest userDetailsRequest, UserDetails userDetails, UUID id) {
        userDetails.setFirstName(userDetailsRequest.getFirstName());
        userDetails.setLastName(userDetailsRequest.getLastName());
        userDetails.setGender(userDetailsRequest.getGender());
        userDetails.setPhoneNumber(userDetailsRequest.getPhoneNumber());
        userDetails.setAvatar(userDetailsRequest.getAvatar());
        userDetails.setUser(userService.getUserById(id));
    }

    @Transactional
    public UserDetails updateUserDetails(@NotNull @Valid UserDetailsRequest userDetailsRequest, @NotNull UUID id) {
        return userDetailsRepository.findById(id).map(userDetails -> {
            modifyUserDetails(userDetailsRequest, userDetails, id);
            return userDetailsRepository.save(userDetails);
        }).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public void updateUserDetails(@NotNull @Valid UserDetails updatedUserDetails) {
        userDetailsRepository.save(updatedUserDetails);
    }
}