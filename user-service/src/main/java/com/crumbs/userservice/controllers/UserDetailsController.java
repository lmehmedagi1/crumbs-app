package com.crumbs.userservice.controllers;

import com.crumbs.userservice.models.UserDetails;
import com.crumbs.userservice.requests.UserDetailsRequest;
import com.crumbs.userservice.services.UserDetailsService;
import com.crumbs.userservice.services.UserService;
import com.crumbs.userservice.utility.UserDetailsModelAssembler;
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

public class UserDetailsController {
    private final UserDetailsService userDetailsService;
    private final UserDetailsModelAssembler userDetailsModelAssembler;
    private final UserService userService;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public UserDetailsController(UserDetailsService userDetailsService,
                                 UserDetailsModelAssembler userDetailsModelAssembler,
                                 UserService userService,
                                 WebClient.Builder webClientBuilder) {
        this.userDetailsService = userDetailsService;
        this.userDetailsModelAssembler = userDetailsModelAssembler;
        this.userService = userService;
        this.webClientBuilder = webClientBuilder;
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public EntityModel<UserDetails> getUserDetails(@RequestParam("id") @NotNull UUID id) {
        return userDetailsModelAssembler.toModel(userDetailsService.getUserDetails(id));
    }

    @PatchMapping(consumes = "application/json")
    public ResponseEntity<?> updateUserDetails(@RequestParam("id") @NotNull UUID id,
                                               @RequestBody @Valid UserDetailsRequest recipeRequest) {
        final UserDetails userDetails = userDetailsService.updateUserDetails(recipeRequest, id);
        EntityModel<UserDetails> entityModel = userDetailsModelAssembler.toModel(userDetails);
        return ResponseEntity.ok(entityModel);
    }

    /**
     * PATCH method with partial update, based on JSON Patch
     */
    @PatchMapping(consumes = "application/json-patch+json")
    public ResponseEntity<?> patchRecipe(@RequestParam("id") @NotNull UUID id, @RequestBody JsonPatch patch) {
        try {
            UserDetails userDetails = userDetailsService.getUserDetails(id);
            final ObjectMapper objectMapper = new ObjectMapper();
            JsonNode patched = patch.apply(objectMapper.convertValue(userDetails, JsonNode.class));
            UserDetails userDetailsPatched = objectMapper.treeToValue(patched, UserDetails.class);
            userDetailsPatched.setUser(userService.getUserById(id));
            userDetailsService.updateUserDetails(userDetailsPatched);
            return ResponseEntity.ok(userDetailsModelAssembler.toModel(userDetailsPatched));
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
