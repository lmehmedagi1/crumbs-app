package com.crumbs.userservice.services;

import com.crumbs.userservice.exceptions.UserNotFoundException;
import com.crumbs.userservice.models.UserDetails;
import com.crumbs.userservice.requests.UserDetailsRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserDetailsServiceTest {
    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    void testGetUserDetailsIncorrectId() {
        assertThrows(UserNotFoundException.class, () -> userDetailsService.getUserDetails(UUID.fromString("d913320a-baf1-43e0-b8b7-25f748e574e2")));
    }

    @Test
    void testGetUserDetailsCorrectId() {
        final UserDetails userDetails = userDetailsService.getUserDetails(UUID.fromString("d913320a-baf1-43e0-b8b7-25f748e574ee"));
        assertAll(
                () -> assertEquals("Medin", userDetails.getFirstName()),
                () -> assertEquals("Paldum", userDetails.getLastName()),
                () -> assertEquals("male", userDetails.getGender()),
                () -> assertEquals("062123123", userDetails.getPhoneNumber()));
    }

    @Test
    void testUpdateUserDetailsSuccess() {
        String avatar = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/HgAGgwJ/lK3Q6wAAAABJRU5ErkJggg==";
        final UserDetailsRequest userDetailsRequest =
                new UserDetailsRequest("Nidem", "Mudlap", "male", "12345689", avatar.getBytes(StandardCharsets.UTF_8));
        final UserDetails userDetails = userDetailsService.updateUserDetails(userDetailsRequest, UUID.fromString("d913320a-baf1-43e0-b8b7-25f748e574ee"));

        assertAll(
                () -> assertEquals("Nidem", userDetails.getFirstName()),
                () -> assertEquals("Mudlap", userDetails.getLastName()),
                () -> assertEquals("male", userDetails.getGender()),
                () -> assertEquals("12345689", userDetails.getPhoneNumber()));
    }
}