package com.crumbs.recipeservice.controllers;

import com.crumbs.recipeservice.exceptions.UserNotFoundException;
import com.crumbs.recipeservice.models.Diet;
import com.crumbs.recipeservice.models.User;
import com.crumbs.recipeservice.requests.DietRequest;
import com.crumbs.recipeservice.services.DietService;
import com.crumbs.recipeservice.utility.DietModelAssembler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/diets")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
public class DietController {

    private final DietService dietService;
    private final DietModelAssembler dietModelAssembler;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public DietController(DietService dietService, DietModelAssembler dietModelAssembler, WebClient.Builder webClientBuilder) {
        this.dietService = dietService;
        this.dietModelAssembler = dietModelAssembler;
        this.webClientBuilder = webClientBuilder;
    }

    public CollectionModel<EntityModel<Diet>> getAllDiets() {
        List<EntityModel<Diet>> diet = dietService.getAllDiets()
                .stream()
                .map(dietModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(diet, linkTo(methodOn(DietController.class).getAllDiets()).withSelfRel());
    }

    @GetMapping
    public CollectionModel<EntityModel<Diet>> getDiets(@RequestParam Map<String, String> allRequestParams)
            throws HttpRequestMethodNotSupportedException {
        if (allRequestParams != null && !allRequestParams.isEmpty())
            throw new HttpRequestMethodNotSupportedException("GET");

        return getAllDiets();
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public EntityModel<Diet> getDiet(@RequestParam("id") @NotNull UUID id) {
        return dietModelAssembler.toModel(dietService.getDiet(id));
    }

    @PostMapping
    public ResponseEntity<?> createDiet(@RequestBody @Valid DietRequest dietRequest) {
        checkIfUserExists(UUID.fromString(dietRequest.getUser_id()));

        final Diet diet = dietService.saveDiet(dietRequest);
        EntityModel<Diet> entityModel = dietModelAssembler.toModel(diet);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PatchMapping(consumes = "application/json")
    public ResponseEntity<?> updateDiet(@RequestParam("id") @NotNull UUID id, @RequestBody @Valid DietRequest dietRequest) {
        checkIfUserExists(UUID.fromString(dietRequest.getUser_id()));

        final Diet diet = dietService.updateDiet(dietRequest, id);
        EntityModel<Diet> entityModel = dietModelAssembler.toModel(diet);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    /**
     * PATCH method with partial update, based on JSON Patch
     */
    @PatchMapping(consumes = "application/json-patch+json")
    public ResponseEntity<?> patchDiet(@RequestParam("id") @NotNull UUID id, @RequestBody JsonPatch patch) {
        try {
            Diet diet = dietService.getDiet(id);
            final ObjectMapper objectMapper = new ObjectMapper();
            JsonNode patched = patch.apply(objectMapper.convertValue(diet, JsonNode.class));
            Diet dietPatched = objectMapper.treeToValue(patched, Diet.class);
            dietService.updateDiet(dietPatched);
            return ResponseEntity.ok(dietModelAssembler.toModel(dietPatched));
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDiet(@RequestParam("id") @NotNull UUID id) {
        dietService.deleteDiet(id);
        return ResponseEntity.noContent().build();
    }

    private User checkIfUserExists(UUID userId) {
        return webClientBuilder.baseUrl("http://user-service").build().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/account")
                        .queryParam("id", userId)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new UserNotFoundException()))
                .bodyToMono(User.class).block();
    }
}
