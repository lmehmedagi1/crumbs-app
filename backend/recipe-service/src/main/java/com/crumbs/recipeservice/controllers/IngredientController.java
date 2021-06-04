package com.crumbs.recipeservice.controllers;

import com.crumbs.recipeservice.models.Ingredient;
import com.crumbs.recipeservice.projections.IngredientView;
import com.crumbs.recipeservice.requests.IngredientRequest;
import com.crumbs.recipeservice.requests.OptionRequest;
import com.crumbs.recipeservice.services.IngredientService;
import com.crumbs.recipeservice.utility.assemblers.IngredientModelAssembler;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/ingredients")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
public class IngredientController {

    private final IngredientService ingredientService;
    private final IngredientModelAssembler ingredientModelAssembler;

    @Autowired
    public IngredientController(IngredientService ingredientService, IngredientModelAssembler ingredientModelAssembler) {
        this.ingredientService = ingredientService;
        this.ingredientModelAssembler = ingredientModelAssembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Ingredient>> getIngredients(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "name") String sort) {
        List<EntityModel<Ingredient>> ingredients = ingredientService.getIngredients(pageNo, pageSize, sort)
                .stream()
                .map(ingredientModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(ingredients, linkTo(methodOn(IngredientController.class).getIngredients(pageNo, pageSize, sort)).withSelfRel());
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public EntityModel<Ingredient> getIngredient(@RequestParam("id") @NotNull UUID id) {
        return ingredientModelAssembler.toModel(ingredientService.getIngredient(id));
    }

    @PostMapping
    public ResponseEntity<?> createIngredient(@RequestBody @Valid IngredientRequest ingredientRequest) {
        final Ingredient ingredient = ingredientService.saveIngredient(ingredientRequest);
        EntityModel<Ingredient> entityModel = ingredientModelAssembler.toModel(ingredient);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PatchMapping(consumes = "application/json")
    public ResponseEntity<?> updateIngredient(@RequestParam("id") @NotNull UUID id, @RequestBody @Valid IngredientRequest ingredientRequest) {
        final Ingredient ingredient = ingredientService.updateIngredient(ingredientRequest, id);
        EntityModel<Ingredient> entityModel = ingredientModelAssembler.toModel(ingredient);
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    /**
     * PATCH method with partial update, based on JSON Patch
     */
    @PatchMapping(consumes = "application/json-patch+json")
    public ResponseEntity<?> patchIngredient(@RequestParam("id") @NotNull UUID id, @RequestBody JsonPatch patch) {
        try {
            Ingredient ingredient = ingredientService.getIngredient(id);
            final ObjectMapper objectMapper = new ObjectMapper();
            JsonNode patched = patch.apply(objectMapper.convertValue(ingredient, JsonNode.class));
            Ingredient ingredientPatched = objectMapper.treeToValue(patched, Ingredient.class);
            ingredientService.updateIngredient(ingredientPatched);
            return ResponseEntity.ok(ingredientModelAssembler.toModel(ingredientPatched));
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteRecipe(@RequestParam("id") @NotNull UUID id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("type")
    @Transactional(readOnly = true)
    public List<IngredientView> searchIngredients(@RequestBody @Valid OptionRequest optionRequest) {
        return ingredientService.searchIngredients(optionRequest);
    }
}
