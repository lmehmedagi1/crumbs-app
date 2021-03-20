package com.crumbs.recipeservice.controllers;

import com.crumbs.recipeservice.exceptions.IngredientNotFoundException;
import com.crumbs.recipeservice.models.Ingredient;
import com.crumbs.recipeservice.requests.IngredientRequest;
import com.crumbs.recipeservice.services.IngredientService;
import com.crumbs.recipeservice.util.IngredientModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class IngredientController {

    @Autowired
    private final IngredientService ingredientService;
    private final IngredientModelAssembler ingredientModelAssembler;

    public IngredientController(IngredientService ingredientService, IngredientModelAssembler ingredientModelAssembler) {
        this.ingredientService = ingredientService;
        this.ingredientModelAssembler = ingredientModelAssembler;
    }

    @GetMapping("/ingredients")
    public CollectionModel<EntityModel<Ingredient>> getAllIngredients() {
        List<EntityModel<Ingredient>> employees = ingredientService.getIngredients().stream()
                .map(ingredientModelAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(employees, linkTo(methodOn(IngredientController.class).getAllIngredients()).withSelfRel());
    }

    @GetMapping("/ingredients/{id}")
    public EntityModel<Ingredient> getIngredient(@PathVariable String id) {
        return ingredientModelAssembler.toModel(ingredientService.getIngredient(id));
    }

    @PostMapping("/ingredients")
    public ResponseEntity<?> createIngredient(@RequestBody @Valid IngredientRequest ingredientRequest) {
        try {
            EntityModel<Ingredient> entityModel = ingredientModelAssembler.toModel(ingredientService.createIngredient(ingredientRequest));
            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PatchMapping("/ingredients/{id}")
    public ResponseEntity<?> updateIngredient(@RequestBody @Valid IngredientRequest ingredientRequest, @PathVariable String id) {
        EntityModel<Ingredient> entityModel = ingredientModelAssembler.toModel(ingredientService.updateIngredient(ingredientRequest, id));
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    @DeleteMapping("/ingredients/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable String id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }
}
