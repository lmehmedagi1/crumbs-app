package com.crumbs.userservice.services;

import com.crumbs.userservice.exceptions.*;
import com.crumbs.userservice.models.*;
import com.crumbs.userservice.projections.UserClassView;
import com.crumbs.userservice.projections.UserView;
import com.crumbs.userservice.repositories.*;
import com.crumbs.userservice.requests.NotificationRequest;
import com.crumbs.userservice.requests.RegisterRequest;
import com.crumbs.userservice.requests.UserUpdateRequest;
import com.crumbs.userservice.requests.UserWebClientRequest;
import com.crumbs.userservice.responses.UserListResponse;
import com.crumbs.userservice.utility.UserSpec;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.crumbs.userservice.utility.Constants.*;

@Service
@Validated
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final EmailService emailService;
    private final UserWebClientRequest webClientRequest;

    @Autowired
    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository, RefreshTokenRepository refreshTokenRepository, VerificationTokenRepository verificationTokenRepository, PasswordResetTokenRepository passwordResetTokenRepository, SubscriptionRepository subscriptionRepository, EmailService emailService, UserWebClientRequest webClientRequest) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.emailService = emailService;
        this.webClientRequest = webClientRequest;
    }

    @Transactional(readOnly = true)
    public User getUserById(@NotNull UUID id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("Specified ID does not exists!"));
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

    @Transactional(readOnly = true)
    public UserListResponse filterUsers(String search, Integer pageNo, Integer pageSize) {

        Specification<User> specification = Specification.where(null);
        if (search != null && !search.isEmpty()) specification = specification.and(UserSpec.isSearched(search));

        Sort sorting = Sort.by("email").ascending();
        Pageable paging = PageRequest.of(pageNo, pageSize, sorting);

        Slice<UserView> slicedProducts = userRepository.findAll(specification, UserView.class, paging);
        List<UserView> products = slicedProducts.getContent();
        return new UserListResponse(products, slicedProducts.hasNext());
    }

    @Transactional(readOnly = true)
    public UserView getUserInfoById(UUID id) {
        return userRepository.findOneById(id);
    }

    @Transactional(readOnly = true)
    public List<UserClassView> getUserSubscribers(UUID id) {
        return userRepository.getUserSubscribers(id);
    }

    @Transactional(readOnly = true)
    public List<UserClassView> getUserSubscriptions(UUID id) {
        return userRepository.getUserSubscriptions(id);
    }

    @Transactional(readOnly = true)
    public boolean checkIfUserIsSubscribed(UUID userId, UUID id) {
        Subscription subscription =  subscriptionRepository.findById(new SubscriptionId(id, userId)).orElse(null);
        return subscription != null;
    }

    @Transactional
    public void subscribe(UUID subscriberId, UUID userId) {
        Subscription subscription = subscriptionRepository.findById(new SubscriptionId(userId, subscriberId)).orElse(null);

        User subscriber = userRepository.findById(subscriberId).orElseThrow(UserNotFoundException::new);
        User author = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (subscription == null) {
            subscription = new Subscription();
            subscription.setId(new SubscriptionId(userId, subscriberId));
            subscription.setSubscriber(subscriber);
            subscription.setAuthor(author);
            subscription.setCreatedAt(LocalDateTime.now(ZoneId.of(DEFAULT_TIMEZONE)));
            subscriptionRepository.save(subscription);

            NotificationRequest notification = new NotificationRequest();
            notification.setDescription(subscriber.getUserProfile().getFirstName() + " " + subscriber.getUserProfile().getLastName() + " has subscribed");
            notification.setTitle("Subscription");
            notification.setUserId(author.getId());
            notification.setEntityId(subscriber.getId());
            notification.setEntityType(NotificationRequest.EntityType.crumbs_user);
            webClientRequest.sendUserSubscribedNotification(notification);
        }
        else subscriptionRepository.delete(subscription);
    }

    @Transactional(readOnly = true)
    public UUID[] getUserSubscribersIds(UUID id) {
        return userRepository.getUserSubscribersIds(id);
    }

    @Transactional
    public User updateUserInfo(UserUpdateRequest userUpdateRequest, UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Specified ID does not exists!"));

        user.getUserProfile().setFirstName(userUpdateRequest.getFirstName());
        user.getUserProfile().setLastName(userUpdateRequest.getLastName());
        user.getUserProfile().setGender(userUpdateRequest.getGender());
        user.getUserProfile().setPhoneNumber(userUpdateRequest.getPhoneNumber());
        user.getUserProfile().setAvatar(userUpdateRequest.getAvatar());

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserClassView getUserViewById(UUID id) {
        UserClassView user = userRepository.getUserPreview(id);
        if (user == null) throw new UserNotFoundException();
        return user;
    }

    @Transactional
    public void sendResetPasswordEmail(String email) {

        final User user = userRepository.findByEmail(email);

        if (user == null) throw new UserNotFoundException();

        if (passwordResetTokenRepository.findByUser_Id(user.getId()) != null)
            throw new IllegalArgumentException("Email was already sent");

        final String value = RandomStringUtils.randomAlphanumeric(18);

        PasswordResetToken emailToken = new PasswordResetToken();
        emailToken.setValue(value);
        emailToken.setUser(user);
        emailToken.setValidUntil(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME));

        passwordResetTokenRepository.save(emailToken);

        final String userName = user.getUserProfile().getFirstName() + " " + user.getUserProfile().getLastName();
        emailService.sendPasswordResetEmail(user.getEmail(), value, userName);
    }

    @Transactional
    public User resetPassword(String value, String password) {

        final PasswordResetToken emailToken = passwordResetTokenRepository.findByValue(value);

        if (emailToken == null)
            throw new UnauthorizedException("Invalid token");

        if (emailToken.getValidUntil().before(new Date())) {
            passwordResetTokenRepository.deleteById(emailToken.getId());
            throw new UnauthorizedException("Reset password token has expired");
        }

        final User user = userRepository.findByEmail(emailToken.getUser().getEmail());

        if (user == null) throw new UserNotFoundException();

        passwordResetTokenRepository.deleteById(emailToken.getId());

        user.setPassword((new BCryptPasswordEncoder()).encode(password));
        userRepository.save(user);
        return user;
    }
}
