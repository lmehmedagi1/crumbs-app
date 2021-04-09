package com.crumbs.userservice.controllers;

import com.crumbs.userservice.models.UserProfile;
import com.crumbs.userservice.requests.UserProfileRequest;
import com.crumbs.userservice.services.UserProfileService;
import com.crumbs.userservice.services.UserService;
import com.crumbs.userservice.utility.UserProfileModelAssembler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequestMapping(value = "/profile")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})

public class UserProfileController {
    private final UserProfileService userProfileService;
    private final UserProfileModelAssembler userProfileModelAssembler;
    private final UserService userService;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public UserProfileController(UserProfileService userProfileService,
                                 UserProfileModelAssembler userProfileModelAssembler,
                                 UserService userService,
                                 WebClient.Builder webClientBuilder) {
        this.userProfileService = userProfileService;
        this.userProfileModelAssembler = userProfileModelAssembler;
        this.userService = userService;
        this.webClientBuilder = webClientBuilder;
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public EntityModel<UserProfile> getUserProfile(@RequestParam("id") @NotNull UUID id) {
        return userProfileModelAssembler.toModel(userProfileService.getUserProfile(id));
    }

    @PatchMapping(consumes = "application/json")
    public ResponseEntity<?> updateUserProfile(@RequestParam("id") @NotNull UUID id,
                                               @RequestBody @Valid UserProfileRequest recipeRequest) {
        final UserProfile userProfile = userProfileService.updateUserProfile(recipeRequest, id);
        EntityModel<UserProfile> entityModel = userProfileModelAssembler.toModel(userProfile);
        return ResponseEntity.ok(entityModel);
    }

    /**
     * PATCH method with partial update, based on JSON Patch
     */
    @PatchMapping(consumes = "application/json-patch+json")
    public ResponseEntity<?> patchRecipe(@RequestParam("id") @NotNull UUID id, @RequestBody JsonPatch patch) {
        try {
            UserProfile userProfile = userProfileService.getUserProfile(id);
            final ObjectMapper objectMapper = new ObjectMapper();
            JsonNode patched = patch.apply(objectMapper.convertValue(userProfile, JsonNode.class));
            UserProfile userProfilePatched = objectMapper.treeToValue(patched, UserProfile.class);
            userProfilePatched.setUser(userService.getUserById(id));
            userProfileService.updateUserProfile(userProfilePatched);
            return ResponseEntity.ok(userProfileModelAssembler.toModel(userProfilePatched));
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
