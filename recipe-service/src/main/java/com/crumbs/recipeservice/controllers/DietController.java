package com.crumbs.recipeservice.controllers;

import com.crumbs.recipeservice.models.Diet;
import com.crumbs.recipeservice.models.User;
import com.crumbs.recipeservice.projections.RecipeView;
import com.crumbs.recipeservice.projections.UserView;
import com.crumbs.recipeservice.requests.DietRequest;
import com.crumbs.recipeservice.requests.WebClientRequest;
import com.crumbs.recipeservice.responses.DietWithDetails;
import com.crumbs.recipeservice.services.DietService;
import com.crumbs.recipeservice.utility.assemblers.DietModelAssembler;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
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
    private final WebClientRequest webClientRequest;

    @Autowired
    public DietController(DietService dietService, DietModelAssembler dietModelAssembler, WebClientRequest webClientRequest) {
        this.dietService = dietService;
        this.dietModelAssembler = dietModelAssembler;
        this.webClientRequest = webClientRequest;
    }

    @GetMapping
    public CollectionModel<EntityModel<Diet>> getDiets(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "title") String sort) {

        List<EntityModel<Diet>> diet = dietService.getDiets(pageNo, pageSize, sort)
                .stream()
                .map(dietModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(diet, linkTo(methodOn(DietController.class).getDiets(pageNo, pageSize, sort)).withSelfRel());
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public EntityModel<Diet> getDiet(@RequestParam("id") @NotNull UUID id) {
        return dietModelAssembler.toModel(dietService.getDiet(id));
    }

    @RequestMapping(params = {"id", "details"}, method = RequestMethod.GET)
    public EntityModel<?> getDietWithDetails(@RequestParam("id") @NotNull UUID id, @RequestParam(value = "details", defaultValue = "false") @NotNull Boolean details) {
        if (!details)
            return getDiet(id);
        else {
            Diet diet = dietService.getDiet(id);

            User author = webClientRequest.getAuthorIfExists(diet.getUser_id());
            DietWithDetails dietWithDetails = new DietWithDetails(diet.getTitle(), diet.getDescription(), diet.getDuration());
            dietWithDetails.setAuthor(new UserView(author.getId(), author.getUsername(), author.getUserProfile().getAvatar()));
            dietWithDetails.setRecipes(diet.getRecipes().stream().map(recipe -> {
                        RecipeView recipeView = new RecipeView(recipe.getId(), recipe.getTitle(), recipe.getDescription(), recipe.getId());
                        if (!recipe.getImages().isEmpty())
                            recipeView.setImage(recipe.getImages().get(0).getImage());

                        User recipeAuthor = webClientRequest.getRecipeAuthor(recipe.getUserId());
                        recipeView.setAuthor(new UserView(recipeAuthor.getId(), recipeAuthor.getUsername(), recipeAuthor.getUserProfile().getAvatar()));
                        return recipeView;
                    }
            ).collect(Collectors.toList()));

            return EntityModel.of(dietWithDetails);
        }
    }

    @PostMapping
    public ResponseEntity<?> createDiet(@RequestBody @Valid DietRequest dietRequest) {
        webClientRequest.getAuthorIfExists(UUID.fromString(dietRequest.getUser_id()));

        final Diet diet = dietService.saveDiet(dietRequest);
        EntityModel<Diet> entityModel = dietModelAssembler.toModel(diet);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PatchMapping(consumes = "application/json")
    public ResponseEntity<?> updateDiet(@RequestParam("id") @NotNull UUID id, @RequestBody @Valid DietRequest dietRequest) {
        webClientRequest.getAuthorIfExists(UUID.fromString(dietRequest.getUser_id()));

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
}