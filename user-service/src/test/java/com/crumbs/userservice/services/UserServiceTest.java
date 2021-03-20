package com.crumbs.userservice.services;

import com.crumbs.userservice.exceptions.IncorrectPasswordException;
import com.crumbs.userservice.exceptions.UserAlreadyExistsException;
import com.crumbs.userservice.exceptions.UserNotFoundException;
import com.crumbs.userservice.models.User;
import com.crumbs.userservice.requests.RegisterRequest;
import com.crumbs.userservice.utility.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testGenerateTokenNullInputParameter() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.generateToken(null);
        });
    }

    @Test
    void testGenerateTokenSuccess() {
        final String generatedToken = userService.generateToken("lmehmedagi");
        final String extractedUsername = JWTUtil.extractUsername(generatedToken);
        assertEquals("lmehmedagi", extractedUsername);
    }

    @Test
    void testGenerateTokenWrongUsername() {
        assertThrows(UserNotFoundException.class, () -> userService.generateToken("testtest"));
    }

    @Test
    void testGetUserByPasswordAndUsernameNullInputParameter() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> {
                    userService.getUser(null, null);
                }),
                () -> assertThrows(IllegalArgumentException.class, () -> {
                    userService.getUser(null, "password");
                }),
                () -> assertThrows(IllegalArgumentException.class, () -> {
                    userService.getUser("lmehmedagi1", null);
                })
        );
    }

    @Test
    void testGetUserByPasswordAndUsernameSuccess() {
        final User user = userService.getUser("lmehmedagi", "Password123");
        assertAll(
                () -> assertEquals("lmehmedagi1@etf.unsa.ba", user.getEmail()),
                () -> assertEquals("Lejla", user.getUserDetails().getFirstName()),
                () -> assertEquals("Mehmedagic", user.getUserDetails().getLastName()));
    }

    @Test
    void testGetUserByPasswordAndUsernameIncorrectPassword() {
        assertThrows(IncorrectPasswordException.class, () -> userService.getUser("lmehmedagi", "pasword"));
    }

    @Test
    void testGetUserByPasswordAndUsernameIncorrectUsername() {
        assertThrows(UserNotFoundException.class, () -> userService.getUser("testtest", "pasword"));
    }

    @Test
    void testGetUserByEmailNullInputParameter() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.getUser(null);
        });
    }

    @Test
    void testGetUserByUsernameSuccess() {
        final User user = userService.getUser("lmehmedagi");
        assertAll(
                () -> assertEquals("lmehmedagi1@etf.unsa.ba", user.getEmail()),
                () -> assertEquals("Lejla", user.getUserDetails().getFirstName()),
                () -> assertEquals("Mehmedagic", user.getUserDetails().getLastName()));
    }

    @Test
    void testGetUserByUsernameFail() {
        assertThrows(UserNotFoundException.class, () -> userService.getUser("testetst"));
    }

    @Test
    void testRegisterUserNullInputParameter() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(null);
        });
    }

    @Test
    void testRegisterUserSuccess() {
        final RegisterRequest registerRequest = new RegisterRequest("lejla", "mehmedagic", "lejlatest", "lejlameh@etf.unsa.ba",
                "Pasword123", "male", "062122122");
        final User user = userService.registerUser(registerRequest);
        assertAll(
                () -> assertEquals("lejlameh@etf.unsa.ba", user.getEmail()),
                () -> assertEquals("lejla", user.getUserDetails().getFirstName()),
                () -> assertEquals("mehmedagic", user.getUserDetails().getLastName()));
    }

    @Test
    void testSaveUserEmailTakenFail() {
        final RegisterRequest registerRequest = new RegisterRequest("lejla", "mehmedagic", "lmehmedagi", "lejlameh@etf.unsa.ba",
                "Pasword123", "male", "062122122");

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(registerRequest));
    }
}
