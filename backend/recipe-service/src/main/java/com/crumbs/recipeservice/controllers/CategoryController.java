package com.crumbs.recipeservice.controllers;

import com.crumbs.recipeservice.models.Category;
import com.crumbs.recipeservice.projections.CategoryView;
import com.crumbs.recipeservice.requests.CategoryRequest;
import com.crumbs.recipeservice.requests.OptionRequest;
import com.crumbs.recipeservice.services.CategoryService;
import com.crumbs.recipeservice.utility.assemblers.CategoryModelAssembler;
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

    @GetMapping
    public CollectionModel<EntityModel<Category>> getCategories(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "name") String sort) {

        List<EntityModel<Category>> categories = categoryService.getCategories(pageNo, pageSize, sort)
                .stream().map(categoryModelAssembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(categories, linkTo(methodOn(CategoryController.class).getCategories(pageNo, pageSize, sort)).withSelfRel());
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
            Category category = categoryService.getCategory(id);
            final ObjectMapper objectMapper = new ObjectMapper();
            JsonNode patched = patch.apply(objectMapper.convertValue(category, JsonNode.class));
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

    @PostMapping("type")
    @Transactional(readOnly = true)
    public List<CategoryView> getCategoriesByType(@RequestBody @Valid OptionRequest optionRequest) {

        return categoryService.getCategoryByType(optionRequest);
    }
}
