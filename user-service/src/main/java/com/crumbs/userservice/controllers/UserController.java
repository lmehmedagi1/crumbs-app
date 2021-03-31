package com.crumbs.userservice.controllers;

import com.crumbs.userservice.models.User;
import com.crumbs.userservice.requests.RegisterRequest;
import com.crumbs.userservice.services.UserService;
import com.crumbs.userservice.utility.UserModelAssembler;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    private final UserModelAssembler userModelAssembler;

    @Autowired
    public UserController(UserService userService, UserModelAssembler userModelAssembler) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
    }

    @PostMapping("/register")
    public EntityModel<User> register(@RequestBody @Valid RegisterRequest registerRequest) {
        final User user = userService.registerUser(registerRequest);
        return userModelAssembler.toModel(user);
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public EntityModel<User> getUserById(@RequestParam("id") @NotNull UUID id) {
        return userModelAssembler.toModel(userService.getUserById(id));
    }

    @RequestMapping(params = "username", method = RequestMethod.GET)
    public EntityModel<User> getUserByUsername(@RequestParam("username") @NotNull String username) {
        return userModelAssembler.toModel(userService.getUserByUsername(username));
    }

    @RequestMapping(params = "email", method = RequestMethod.GET)
    public EntityModel<User> getUserByEmail(@RequestParam("email") @NotBlank String email) {
        return userModelAssembler.toModel(userService.getUserByEmail(email));
    }

    @RequestMapping(params = "id", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUserById(@RequestParam("id") @NotBlank String id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
