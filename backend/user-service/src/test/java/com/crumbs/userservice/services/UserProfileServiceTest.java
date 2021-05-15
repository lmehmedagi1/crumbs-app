package com.crumbs.userservice.services;

import com.crumbs.userservice.exceptions.UserNotFoundException;
import com.crumbs.userservice.models.UserProfile;
import com.crumbs.userservice.requests.UserProfileRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserProfileServiceTest {
    @Autowired
    private UserProfileService userProfileService;

    @Test
    void testGetUserDetailsIncorrectId() {
        assertThrows(UserNotFoundException.class, () -> userProfileService.getUserProfile(UUID.fromString("d913320a-baf1-43e0-b8b7-25f748e574e2")));
    }

    @Test
    void testGetUserDetailsCorrectId() {
        final UserProfile userProfile = userProfileService.getUserProfile(UUID.fromString("d913320a-baf1-43e0-b8b7-25f748e574ee"));
        assertAll(
                () -> assertEquals("Medin", userProfile.getFirstName()),
                () -> assertEquals("Paldum", userProfile.getLastName()),
                () -> assertEquals("male", userProfile.getGender()),
                () -> assertEquals("062123123", userProfile.getPhoneNumber()));
    }

    @Test
    void testUpdateUserDetailsSuccess() {
        String avatar = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/HgAGgwJ/lK3Q6wAAAABJRU5ErkJggg==";
        final UserProfileRequest userProfileRequest =
                new UserProfileRequest("Nidem", "Mudlap", "male", "12345689", avatar.getBytes(StandardCharsets.UTF_8));
        final UserProfile userProfile = userProfileService.updateUserProfile(userProfileRequest, UUID.fromString("d913320a-baf1-43e0-b8b7-25f748e574ee"));

        assertAll(
                () -> assertEquals("Nidem", userProfile.getFirstName()),
                () -> assertEquals("Mudlap", userProfile.getLastName()),
                () -> assertEquals("male", userProfile.getGender()),
                () -> assertEquals("12345689", userProfile.getPhoneNumber()));
    }
}