package com.crumbs.userservice.services;

import com.crumbs.userservice.exceptions.UserNotFoundException;
import com.crumbs.userservice.models.UserProfile;
import com.crumbs.userservice.repositories.UserProfileRepository;
import com.crumbs.userservice.requests.UserProfileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Service
@Validated
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserService userService;

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository, UserService userService) {
        this.userProfileRepository = userProfileRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public UserProfile getUserProfile(@NotNull UUID id) {
        return userProfileRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    private void modifyUserProfile(UserProfileRequest userProfileRequest, UserProfile userProfile, UUID id) {
        userProfile.setFirstName(userProfileRequest.getFirstName());
        userProfile.setLastName(userProfileRequest.getLastName());
        userProfile.setGender(userProfileRequest.getGender());
        userProfile.setPhoneNumber(userProfileRequest.getPhoneNumber());
        userProfile.setAvatar(userProfileRequest.getAvatar());
        userProfile.setUser(userService.getUserById(id));
    }

    @Transactional
    public UserProfile updateUserProfile(@NotNull @Valid UserProfileRequest userProfileRequest, @NotNull UUID id) {
        return userProfileRepository.findById(id).map(userProfile -> {
            modifyUserProfile(userProfileRequest, userProfile, id);
            return userProfileRepository.save(userProfile);
        }).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public void updateUserProfile(@NotNull @Valid UserProfile updatedUserProfile) {
        userProfileRepository.save(updatedUserProfile);
    }
}
