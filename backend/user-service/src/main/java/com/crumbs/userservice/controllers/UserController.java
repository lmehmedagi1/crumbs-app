package com.crumbs.userservice.controllers;

import com.crumbs.userservice.jwt.JwtConfigAndUtil;
import com.crumbs.userservice.models.User;
import com.crumbs.userservice.projections.UserClassView;
import com.crumbs.userservice.projections.UserView;
import com.crumbs.userservice.requests.LoginRequest;
import com.crumbs.userservice.requests.RegisterRequest;
import com.crumbs.userservice.requests.UserUpdateRequest;
import com.crumbs.userservice.responses.UserListResponse;
import com.crumbs.userservice.services.CustomUserDetailsService;
import com.crumbs.userservice.services.UserService;
import com.crumbs.userservice.utility.assemblers.UserModelAssembler;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/account")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
public class UserController {

    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserModelAssembler userModelAssembler;
    private final JwtConfigAndUtil jwtConfigAndUtil;

    private UUID getUserIdFromJwt(String jwt) {
        return UUID.fromString(new JwtConfigAndUtil().extractUserId(jwt.substring(7)));
    }

    @Autowired
    public UserController(UserService userService, UserModelAssembler userModelAssembler, JwtConfigAndUtil jwtConfigAndUtil, CustomUserDetailsService customUserDetailsService) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
        this.jwtConfigAndUtil = jwtConfigAndUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public EntityModel<User> getUserByJwt(@RequestHeader("Authorization") String jwt) {
        UUID userId = getUserIdFromJwt(jwt);
        return userModelAssembler.toModel(userService.getUserById(userId));
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public EntityModel<User> getUserById(@RequestParam @NotNull UUID id) {
        return userModelAssembler.toModel(userService.getUserById(id));
    }

//    @RequestMapping(params = "username", method = RequestMethod.GET)
//    public EntityModel<User> getUserByUsername(@RequestParam @NotNull String username) {
//        return userModelAssembler.toModel(userService.getUserByUsername(username));
//    }
//
//    @RequestMapping(params = "email", method = RequestMethod.GET)
//    public EntityModel<User> getUserByEmail(@RequestParam @NotBlank String email) {
//        return userModelAssembler.toModel(userService.getUserByEmail(email));
//    }

    @RequestMapping(params = "id", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUserById(@RequestParam @NotNull UUID id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    //    ISPOD NOVO
    @GetMapping("/all")
    public ResponseEntity<UserListResponse> filterUsers(@RequestParam(defaultValue = "") String search,
                                                           @RequestParam(defaultValue = "0") Integer pageNo,
                                                           @RequestParam(defaultValue = "3") Integer pageSize) {
        return ResponseEntity.ok(userService.filterUsers(search, pageNo, pageSize));
    }

    @GetMapping("/info")
    public ResponseEntity<UserView> getUserInfoById(@RequestParam UUID id) {
        return ResponseEntity.ok(userService.getUserInfoById(id));
    }

    @GetMapping("/view")
    public ResponseEntity<UserClassView> getUserViewById(@RequestParam UUID id) {
        return ResponseEntity.ok(userService.getUserViewById(id));
    }

    @GetMapping("/subscribed")
    public ResponseEntity<Boolean> checkIfUserIsSubscribed(@RequestParam UUID id, @RequestHeader("Authorization") String jwt) {
        UUID userId = getUserIdFromJwt(jwt);
        return ResponseEntity.ok(userService.checkIfUserIsSubscribed(userId, id));
    }

    @GetMapping("/subscribers")
    public ResponseEntity<List<UserClassView>> getUserSubscribers(@RequestParam UUID id) {
        return ResponseEntity.ok(userService.getUserSubscribers(id));
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<List<UserClassView>> getUserSubscriptions(@RequestParam UUID id) {
        return ResponseEntity.ok(userService.getUserSubscriptions(id));
    }

    @PutMapping("/updateInfo")
    public ResponseEntity<User> updateUserInformation(@RequestBody @Valid UserUpdateRequest userUpdateRequest, @RequestHeader("Authorization") String jwt) {
        UUID userId = getUserIdFromJwt(jwt);
        final User user = userService.updateUserInfo(userUpdateRequest, userId);
        return ResponseEntity.ok(user);
    }
}
