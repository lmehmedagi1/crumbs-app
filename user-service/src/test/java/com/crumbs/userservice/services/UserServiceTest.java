package com.crumbs.userservice.services;

import com.crumbs.userservice.exceptions.IncorrectPasswordException;
import com.crumbs.userservice.exceptions.UserAlreadyExistsException;
import com.crumbs.userservice.exceptions.UserNotFoundException;
import com.crumbs.userservice.jwt.JwtConfigAndUtil;
import com.crumbs.userservice.models.User;
import com.crumbs.userservice.requests.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtConfigAndUtil jwtConfigAndUtil;

    @Autowired
    public UserServiceTest(UserService userService, CustomUserDetailsService customUserDetailsService, JwtConfigAndUtil jwtConfigAndUtil) {
        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtConfigAndUtil = jwtConfigAndUtil;
    }

    @Test
    void testGenerateTokenNullInputParameter() {
        assertThrows(NullPointerException.class, () -> {
            jwtConfigAndUtil.generateToken(null);
        });
    }

    @Test
    void testGenerateTokenSuccess() {
        final String generatedToken = jwtConfigAndUtil.generateToken(customUserDetailsService.loadUserByUsername("lmehmedagi"));
        final String extractedUsername = jwtConfigAndUtil.extractUsername(generatedToken);
        assertEquals("lmehmedagi", extractedUsername);
    }

    @Test
    void testGenerateTokenWrongUsername() {
        assertThrows(UserNotFoundException.class, () -> jwtConfigAndUtil.generateToken(customUserDetailsService.loadUserByUsername("testtest")));
    }

    @Test
    void testGetUserByPasswordAndUsernameNullInputParameter() {
        assertAll(
                () -> assertThrows(ConstraintViolationException.class, () -> {
                    userService.getUserByCredentials(null, null);
                }),
                () -> assertThrows(ConstraintViolationException.class, () -> {
                    userService.getUserByCredentials(null, "password");
                }),
                () -> assertThrows(ConstraintViolationException.class, () -> {
                    userService.getUserByCredentials("lmehmedagi1", null);
                })
        );
    }

    @Test
    void testGetUserByPasswordAndUsernameSuccess() {
        final User user = userService.getUserByCredentials("lmehmedagi", "Password123");
        assertAll(
                () -> assertEquals("lmehmedagi1@etf.unsa.ba", user.getEmail()),
                () -> assertEquals("Lejla", user.getUserProfile().getFirstName()),
                () -> assertEquals("Mehmedagic", user.getUserProfile().getLastName()));
    }

    @Test
    void testGetUserByPasswordAndUsernameIncorrectPassword() {
        assertThrows(IncorrectPasswordException.class, () -> userService.getUserByCredentials("lmehmedagi", "pasword"));
    }

    @Test
    void testGetUserByPasswordAndUsernameIncorrectUsername() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserByCredentials("testtest", "pasword"));
    }

    @Test
    void testGetUserByEmailNullInputParameter() {
        assertThrows(ConstraintViolationException.class, () -> {
            userService.getUserByEmail(null);
        });
    }

    @Test
    void testGetUserByUsernameSuccess() {
        final User user = userService.getUserByUsername("lmehmedagi");
        assertAll(
                () -> assertEquals("lmehmedagi1@etf.unsa.ba", user.getEmail()),
                () -> assertEquals("Lejla", user.getUserProfile().getFirstName()),
                () -> assertEquals("Mehmedagic", user.getUserProfile().getLastName()));
    }

    @Test
    void testGetUserByUsernameFail() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("testetst"));
    }

    @Test
    void testRegisterUserSuccess() {
        final RegisterRequest registerRequest = new RegisterRequest("lejlaa_1", "lejlameh@etf.unsa.ba", "Passsword123!", "lejla",
                "mehmedagic", "female", "062122122");

        final User user = userService.registerUser(registerRequest);

        assertAll(
                () -> assertEquals("lejlameh@etf.unsa.ba", user.getEmail()),
                () -> assertEquals("lejla", user.getUserProfile().getFirstName()),
                () -> assertEquals("mehmedagic", user.getUserProfile().getLastName()));
    }

    @Test
    void testSaveUserEmailTakenFail() {
        final RegisterRequest registerRequest = new RegisterRequest("lejlaa_1", "lejlameh@etf.unsa.ba", "Pasword123!", "lejla",
                "mehmedagic", "female", "062122122");

        final User user = userService.registerUser(registerRequest);

        final RegisterRequest registerRequest2 = new RegisterRequest("lejlaa_2", "lejlameh@etf.unsa.ba", "Pasword123!", "lejla",
                "mehmedagic", "female", "062122122");

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(registerRequest));
    }
}
