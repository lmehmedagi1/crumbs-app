package com.crumbs.userservice.controllers;

import com.crumbs.userservice.jwt.JwtConfigAndUtil;
import com.crumbs.userservice.models.User;
import com.crumbs.userservice.requests.LoginRequest;
import com.crumbs.userservice.requests.RegisterRequest;
import com.crumbs.userservice.services.EmailService;
import com.crumbs.userservice.services.UserService;
import com.crumbs.userservice.utility.assemblers.UserModelAssembler;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.UUID;

import static com.crumbs.userservice.utility.Constants.COOKIE_STRING;
import static com.crumbs.userservice.utility.Constants.REFRESH_TOKEN_EXPIRATION_TIME;

@RestController
@RequestMapping("/auth")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
public class AuthController {

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;
    private final JwtConfigAndUtil jwtConfigAndUtil;
    private final EmailService emailService;

    public AuthController(UserService userService, UserModelAssembler userModelAssembler, JwtConfigAndUtil jwtConfigAndUtil, EmailService emailService) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
        this.jwtConfigAndUtil = jwtConfigAndUtil;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public EntityModel<User> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        final User user = userService.getUserByCredentials(loginRequest.getUsername(), loginRequest.getPassword());
        final String jwt = jwtConfigAndUtil.generateToken(user.getId().toString());
        final String refreshToken = userService.generateRefreshToken(user);
        response.setHeader("Access-Control-Expose-Headers", "Authorization, Set-Cookie");
        response.setHeader("Authorization", "Bearer " + jwt);
        response.addCookie(getCookie(refreshToken, REFRESH_TOKEN_EXPIRATION_TIME));
        return userModelAssembler.toModel(user);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest registerRequest) {
        final User user = userService.registerUser(registerRequest);
        final String verificationToken = userService.generateVerificationToken(user);
        final String userName = user.getUserProfile().getFirstName() + " " + user.getUserProfile().getLastName();
        emailService.sendConfirmRegistrationEmail(user.getEmail(), "Registration Confirmation", verificationToken, userName);
        return ResponseEntity.ok("Verification email has been sent to " + user.getEmail());
    }

    @GetMapping("/registration-confirmation")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token, HttpServletResponse response) {
        userService.confirmRegistration(token);
        return ResponseEntity.ok("Account successfully verified");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(COOKIE_STRING) String refreshToken, HttpServletResponse response) {
        userService.logout(refreshToken);
        response.addCookie(getCookie(refreshToken, 0));
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@CookieValue(COOKIE_STRING) String refreshToken, HttpServletResponse response) {
        final UUID userId = userService.getUserIdFromRefreshToken(refreshToken);
        final String jwt = jwtConfigAndUtil.generateToken(userId.toString());
        response.setHeader("Access-Control-Expose-Headers", "Authorization, Set-Cookie");
        response.setHeader("Authorization", "Bearer " + jwt);
        return ResponseEntity.ok("Success");
    }

    private Cookie getCookie(String refreshCookie, int expirationTime) {
        Cookie cookie = new Cookie(COOKIE_STRING, refreshCookie);
        cookie.setMaxAge(expirationTime);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
