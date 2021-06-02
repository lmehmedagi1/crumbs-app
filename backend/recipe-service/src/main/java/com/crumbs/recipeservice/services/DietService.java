package com.crumbs.recipeservice.services;

import com.crumbs.recipeservice.exceptions.DietNotFoundException;
import com.crumbs.recipeservice.exceptions.UnauthorizedException;
import com.crumbs.recipeservice.models.Diet;
import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.projections.DietClassView;
import com.crumbs.recipeservice.projections.RecipeView;
import com.crumbs.recipeservice.projections.UserDietView;
import com.crumbs.recipeservice.repositories.DietRepository;
import com.crumbs.recipeservice.requests.DietRequest;
import com.crumbs.recipeservice.requests.WebClientRequest;
import com.crumbs.recipeservice.responses.DietViewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Validated
public class DietService {

    private final DietRepository dietRepository;
    private final RecipeService recipeService;
    private final WebClientRequest webClientRequest;

    @Autowired
    public DietService(DietRepository dietRepository, RecipeService recipeService, WebClientRequest webClientRequest) {
        this.dietRepository = dietRepository;
        this.recipeService = recipeService;
        this.webClientRequest = webClientRequest;
    }

    @Transactional(readOnly = true)
    public List<Diet> getDiets(Integer pageNo, Integer pageSize, String sort) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sort).ascending());
        Slice<Diet> slicedProducts = dietRepository.findAll(paging);
        return slicedProducts.getContent();
    }

    @Transactional(readOnly = true)
    public DietViewResponse getDietViews(Integer pageNo, Integer pageSize, String sort, String search) {
        Sort sorting = getDietSorting(sort);
        Pageable paging = PageRequest.of(pageNo, pageSize, sorting);

        Slice<DietClassView> slicedProducts = dietRepository.getPublicDiets(search, paging);
        List<DietClassView> diets = slicedProducts.getContent();
        final boolean hasNext = slicedProducts.hasNext();

        for (DietClassView d : diets) {
            Pageable recipePaging = PageRequest.of(0, 3, Sort.by("title").ascending());
            List<RecipeView> dietRecipes = dietRepository.getPublicDietRecipes(d.getId(), recipePaging).getContent();
            for (RecipeView r : dietRecipes) {
                r.setImage(recipeService.getRecipeImage(r.getRecipeId()));
            }
            d.setRecipes(dietRecipes);
            d.setAuthor(webClientRequest.getUserPreview(d.getAuthor().getId()));
        }
        return new DietViewResponse(diets, hasNext);
    }

    @Transactional(readOnly = true)
    public Diet getDiet(@NotNull UUID id) {
        return dietRepository.findById(id).orElseThrow(DietNotFoundException::new);
    }

    private void modifyDiet(DietRequest dietRequest, UUID userId, Diet diet) {
        diet.setTitle(dietRequest.getTitle());
        diet.setDescription(dietRequest.getDescription());
        diet.setDuration(dietRequest.getDuration());
        diet.setIsPrivate(dietRequest.getIs_private());
        diet.setUserId(userId);
        diet.setRecipes(new ArrayList<>());
    }

    @Transactional
    public Diet saveDiet(@NotNull @Valid DietRequest dietRequest, @NotNull UUID userId) {
        Diet diet = new Diet();
        modifyDiet(dietRequest, userId, diet);
        dietRepository.save(diet);
        return diet;
    }

    @Transactional
    public Diet updateDiet(@NotNull @Valid DietRequest dietRequest, @NotNull UUID id, @NotNull UUID userId) {
        Diet diet = dietRepository.findByIdAndUserId(id, userId);
        if (diet == null)
            throw new UnauthorizedException("You don't have permission to update this diet");

        modifyDiet(dietRequest, userId, diet);
        return dietRepository.save(diet);
    }

    @Transactional
    public void updateDiet(@NotNull @Valid Diet updatedDiet) {
        dietRepository.save(updatedDiet);
    }

    @Transactional
    public void deleteDiet(@NotNull UUID id, @NotNull UUID userId) {
        if (dietRepository.findByIdAndUserId(id, userId) == null)
            throw new DietNotFoundException("You don't have permission to delete this diet");

        dietRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<UserDietView> getUserDiets(UUID id) {
        return dietRepository.findByUserId(id);
    }

    @Transactional(readOnly = true)
    public String getDietImage(UUID id) {
        List<Recipe> recipes = dietRepository.getDietRecipes(id);
        if (!recipes.isEmpty() && !recipes.get(0).getImages().isEmpty()) return recipes.get(0).getImages().get(0).getImage();
        return null;
    }

    private Sort getDietSorting(String sort) {
        String sortBy = sort.split("-")[0];
        return sort.split("-")[1].equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    }
}
