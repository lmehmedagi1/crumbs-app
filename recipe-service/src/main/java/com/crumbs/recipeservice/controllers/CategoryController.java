package com.crumbs.recipeservice.controllers;

import com.crumbs.recipeservice.models.Category;
import com.crumbs.recipeservice.requests.CategoryRequest;
import com.crumbs.recipeservice.services.CategoryService;
import com.crumbs.recipeservice.utility.CategoryModelAssembler;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/categories")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryModelAssembler categoryModelAssembler;

    @Autowired
    public CategoryController(CategoryService categoryService, CategoryModelAssembler categoryModelAssembler) {
        this.categoryService = categoryService;
        this.categoryModelAssembler = categoryModelAssembler;
    }

    public CollectionModel<EntityModel<Category>> getAllCategories() {
        List<EntityModel<Category>> reviews = categoryService.getAllCategories()
                .stream()
                .map(categoryModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reviews, linkTo(methodOn(CategoryController.class).getAllCategories()).withSelfRel());
    }

    @GetMapping
    public CollectionModel<EntityModel<Category>> getCategories(@RequestParam Map<String, String> allRequestParams)
            throws HttpRequestMethodNotSupportedException {
        if (allRequestParams != null && !allRequestParams.isEmpty())
            throw new HttpRequestMethodNotSupportedException("GET");

        return getAllCategories();
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public EntityModel<Category> getCategory(@RequestParam("id") @NotNull UUID id) {
        return categoryModelAssembler.toModel(categoryService.getCategory(id));
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        final Category category = categoryService.saveCategory(categoryRequest);
        EntityModel<Category> entityModel = categoryModelAssembler.toModel(category);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PatchMapping(consumes = "application/json")
    public ResponseEntity<?> updateCategory(@RequestParam("id") @NotNull UUID id, @RequestBody @Valid CategoryRequest categoryRequest) {
        final Category category = categoryService.updateCategory(categoryRequest, id);
        EntityModel<Category> entityModel = categoryModelAssembler.toModel(category);
        return ResponseEntity.ok(entityModel);
    }

    /**
     * PATCH method with partial update, based on JSON Patch
     */
    @PatchMapping(consumes = "application/json-patch+json")
    public ResponseEntity<?> patchCategory(@RequestParam("id") @NotNull UUID id, @RequestBody JsonPatch patch) {
        try {
            Category review = categoryService.getCategory(id);
            final ObjectMapper objectMapper = new ObjectMapper();
            JsonNode patched = patch.apply(objectMapper.convertValue(review, JsonNode.class));
            Category categoryPatched = objectMapper.treeToValue(patched, Category.class);
            categoryService.updateCategory(categoryPatched);
            return ResponseEntity.ok(categoryModelAssembler.toModel(categoryPatched));
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCategory(@RequestParam("id") @NotNull UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
