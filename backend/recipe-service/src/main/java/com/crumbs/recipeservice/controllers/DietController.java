package com.crumbs.recipeservice.controllers;

import com.crumbs.recipeservice.models.Diet;
import com.crumbs.recipeservice.models.User;
import com.crumbs.recipeservice.projections.DietClassView;
import com.crumbs.recipeservice.projections.RecipeView;
import com.crumbs.recipeservice.projections.UserClassView;
import com.crumbs.recipeservice.projections.UserDietView;
import com.crumbs.recipeservice.projections.UserView;
import com.crumbs.recipeservice.requests.DietRequest;
import com.crumbs.recipeservice.requests.WebClientRequest;
import com.crumbs.recipeservice.responses.DietViewResponse;
import com.crumbs.recipeservice.responses.DietWithDetails;
import com.crumbs.recipeservice.services.DietService;
import com.crumbs.recipeservice.utility.JwtConfigAndUtil;
import com.crumbs.recipeservice.utility.assemblers.DietModelAssembler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<DietViewResponse> getDiets(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "3") Integer pageSize,
            @RequestParam(defaultValue = "title-asc") String sort,
            @RequestParam(defaultValue = "") String search) {
        return ResponseEntity.ok(dietService.getDietViews(pageNo, pageSize, sort, search));
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public EntityModel<Diet> getDiet(@RequestParam("id") @NotNull UUID id) {
        return dietModelAssembler.toModel(dietService.getDiet(id));
    }

    @RequestMapping(params = {"id", "details"}, method = RequestMethod.GET)
    public EntityModel<?> getDietWithDetails(@RequestParam("id") @NotNull UUID id,
                                             @RequestParam(value = "details", defaultValue = "false")
                                             @NotNull Boolean details,
                                             @RequestHeader("Authorization") String jwt) {
        if (!details)
            return getDiet(id);
        else {
            Diet diet = dietService.getDiet(id);

            User author = webClientRequest.checkIfUserExists(jwt);
            DietWithDetails dietWithDetails = new DietWithDetails(diet.getTitle(), diet.getDescription(), diet.getDuration());
            dietWithDetails.setAuthor(new UserView(author.getId(), author.getUsername(), author.getUserProfile().getAvatar()));
            dietWithDetails.setRecipes(diet.getRecipes().stream().map(recipe -> {
                        RecipeView recipeView = new RecipeView(recipe.getId(), recipe.getTitle(), recipe.getDescription(), recipe.getId());
                        if (!recipe.getImages().isEmpty())
                            recipeView.setImage(recipe.getImages().get(0).getImage());

                        UserClassView recipeAuthor = webClientRequest.getUserPreview(recipe.getUserId());
                        recipeView.setAuthor(recipeAuthor);
                        return recipeView;
                    }
            ).collect(Collectors.toList()));

            return EntityModel.of(dietWithDetails);
        }
    }

    @PostMapping
    public ResponseEntity<?> createDiet(@RequestBody @Valid DietRequest dietRequest, @RequestHeader("Authorization") String jwt) {
        webClientRequest.checkIfUserExists(jwt);
        UUID userId = JwtConfigAndUtil.getUserIdFromJwt(jwt);
        final Diet diet = dietService.saveDiet(dietRequest, userId);
        EntityModel<Diet> entityModel = dietModelAssembler.toModel(diet);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PatchMapping(consumes = "application/json")
    public ResponseEntity<?> updateDiet(@RequestParam("id") @NotNull UUID id,
                                        @RequestBody @Valid DietRequest dietRequest,
                                        @RequestHeader("Authorization") String jwt) {

        webClientRequest.checkIfUserExists(jwt);
        UUID userId = JwtConfigAndUtil.getUserIdFromJwt(jwt);
        final Diet diet = dietService.updateDiet(dietRequest, id, userId);
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
    public ResponseEntity<?> deleteDiet(@RequestParam("id") @NotNull UUID id, @RequestHeader("Authorization") String jwt) {
        UUID userId = JwtConfigAndUtil.getUserIdFromJwt(jwt);
        dietService.deleteDiet(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserDietView>> getUserDiets(@RequestParam UUID id) {
        List<UserDietView> diets = dietService.getUserDiets(id);
        for (UserDietView diet : diets) {
            diet.setImage(dietService.getDietImage(diet.getId()));
            //diet.setRating(webClientRequest.getDietRating(diet.getId()));
        }
        return ResponseEntity.ok(diets);
    }
}
