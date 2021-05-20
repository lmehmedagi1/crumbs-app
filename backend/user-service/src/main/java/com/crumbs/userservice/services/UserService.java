package com.crumbs.userservice.services;

import com.crumbs.userservice.exceptions.IncorrectPasswordException;
import com.crumbs.userservice.exceptions.TokenExpiredException;
import com.crumbs.userservice.exceptions.UnauthorizedException;
import com.crumbs.userservice.exceptions.UserAlreadyExistsException;
import com.crumbs.userservice.exceptions.UserNotFoundException;
import com.crumbs.userservice.models.RefreshToken;
import com.crumbs.userservice.models.User;
import com.crumbs.userservice.models.UserProfile;
import com.crumbs.userservice.models.VerificationToken;
import com.crumbs.userservice.repositories.RefreshTokenRepository;
import com.crumbs.userservice.repositories.UserProfileRepository;
import com.crumbs.userservice.repositories.UserRepository;
import com.crumbs.userservice.repositories.VerificationTokenRepository;
import com.crumbs.userservice.requests.RegisterRequest;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

import static com.crumbs.userservice.utility.Constants.REFRESH_TOKEN_EXPIRATION_TIME;
import static com.crumbs.userservice.utility.Constants.VERIFICATION_TOKEN_EXPIRATION_TIME;

@Service
@Validated
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository, RefreshTokenRepository refreshTokenRepository, VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.verificationTokenRepository = verificationTokenRepository;
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
        if (!user.getUserProfile().isEmailVerified())
            throw new UnauthorizedException("This account has not been verified yet");
        return user;
    }

    @Transactional
    public User registerUser(@NotNull @Valid RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()) != null)
            throw new UserAlreadyExistsException("Username already taken, try another one!");
        else if (userRepository.findByEmail(registerRequest.getEmail()) != null)
            throw new UserAlreadyExistsException("Email already taken, try another one!");

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword((new BCryptPasswordEncoder(10)).encode(registerRequest.getPassword()));

        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName(registerRequest.getFirst_name());
        userProfile.setLastName(registerRequest.getLast_name());
        userProfile.setPhoneNumber(registerRequest.getPhone_number());
        userProfile.setGender(registerRequest.getGender());
        userProfile.setEmailVerified(false);

        user.setUserProfile(userProfile);
        userProfile.setUser(user);

        userRepository.save(user);
        userProfileRepository.save(userProfile);
        return user;
    }

    @Transactional
    public void deleteUserById(@NotNull UUID id) {
        if (!userRepository.existsById(id))
            throw new UserNotFoundException("Specified ID does not exists!");

        userRepository.deleteById(id);
    }

    @Transactional
    public String generateRefreshToken(@NotNull User user) {
        String value = "";

        final RefreshToken token = refreshTokenRepository.findByUser_Id(user.getId());

        if (token != null) {
            value = token.getValue();
            token.setValidUntil(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME));
            refreshTokenRepository.save(token);
        }
        else {
            value = RandomStringUtils.randomAlphanumeric(18);
            RefreshToken newToken = new RefreshToken();
            newToken.setValue(value);
            newToken.setUser(user);
            newToken.setValidUntil(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME));
            refreshTokenRepository.save(newToken);
        }
        return value;
    }

    @Transactional
    public UUID getUserIdFromRefreshToken(@NotNull String refreshToken) {

        final RefreshToken expectedToken = refreshTokenRepository.findByValue(refreshToken);

        if (expectedToken == null)
            throw new UnauthorizedException("Invalid token");

        if (expectedToken.getValidUntil().before(new Date())) {
            refreshTokenRepository.delete(expectedToken);
            throw new TokenExpiredException("Refresh token has expired");
        }

        return expectedToken.getUser().getId();
    }

    @Transactional
    public void logout(@NotNull String refreshToken) {
        final RefreshToken expectedToken = refreshTokenRepository.findByValue(refreshToken);

        if (expectedToken == null)
            throw new UnauthorizedException("Invalid token");

        refreshTokenRepository.delete(expectedToken);
    }

    @Transactional
    public String generateVerificationToken(User user) {
        final String value = RandomStringUtils.randomAlphanumeric(18);
        VerificationToken newToken = new VerificationToken();
        newToken.setValue(value);
        newToken.setUser(user);
        newToken.setValidUntil(new Date(System.currentTimeMillis() + VERIFICATION_TOKEN_EXPIRATION_TIME));
        verificationTokenRepository.save(newToken);
        return value;
    }

    @Transactional
    public void confirmRegistration(String token) {
        final VerificationToken verificationToken = verificationTokenRepository.findByValue(token);
        if (verificationToken == null)
            throw new UnauthorizedException("Invalid token");
        else if (verificationToken.getValidUntil().before(new Date())) {
            userRepository.deleteById(verificationToken.getUser().getId());
            verificationTokenRepository.deleteById(verificationToken.getId());
            throw new UnauthorizedException("Token has expired");
        }

        verificationToken.getUser().getUserProfile().setEmailVerified(true);
        userProfileRepository.save(verificationToken.getUser().getUserProfile());
    }
}
