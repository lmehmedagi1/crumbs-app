package com.crumbs.recipeservice.services;

import com.crumbs.recipeservice.exceptions.DietNotFoundException;
import com.crumbs.recipeservice.exceptions.UnauthorizedException;
import com.crumbs.recipeservice.models.Diet;
import com.crumbs.recipeservice.models.Ingredient;
import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.projections.*;
import com.crumbs.recipeservice.repositories.DietRepository;
import com.crumbs.recipeservice.requests.DietRequest;
import com.crumbs.recipeservice.requests.WebClientRequest;
import com.crumbs.recipeservice.responses.DietResponse;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.crumbs.recipeservice.utility.Constants.DEFAULT_TIMEZONE;

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
    public DietResponse getPrivateDiet(@NotNull UUID id, @NotNull UserClassView author) {
        Diet diet = dietRepository.findById(id).orElseThrow(DietNotFoundException::new);
        if (diet.getIsPrivate() && !diet.getUserId().equals(author.getId())) throw new UnauthorizedException("You don't have permission to view this diet");
        return getDiet(diet, author);
    }

    @Transactional(readOnly = true)
    public DietResponse getPublicDiet(@NotNull UUID id) {
        Diet diet = dietRepository.findById(id).orElseThrow(DietNotFoundException::new);
        UserClassView author = webClientRequest.getUserPreview(diet.getUserId());
        return getDiet(diet, author);
    }

    private DietResponse getDiet(Diet diet, UserClassView author) {
        List<RecipeView> recipes = new ArrayList<>();
        for (Recipe r : diet.getRecipes()) {
            RecipeView recipeView = new RecipeView(r.getId(), r.getTitle(), r.getDescription(), r.getUserId());
            if (r.getImages().size() > 0) recipeView.setImage(r.getImages().get(0).getImage());
            recipes.add(recipeView);
        }

        Set<IngredientView> ingredients = new HashSet<>();
        for (Recipe r : diet.getRecipes()) {
            for (Ingredient i : r.getIngredients()) {
                ingredients.add(new IngredientView(i.getId(), i.getName()));
            }
        }
        return new DietResponse(diet.getId(), diet.getTitle(), diet.getDescription(), diet.getDuration(), diet.getIsPrivate(), diet.getCreatedAt(), author, recipes, ingredients);
    }

    private void modifyDiet(DietRequest dietRequest, UUID userId, Diet diet) {
        diet.setTitle(dietRequest.getTitle());
        diet.setDescription(dietRequest.getDescription());
        diet.setDuration(dietRequest.getDuration());
        diet.setIsPrivate(dietRequest.getIsPrivate());
        diet.setUserId(userId);
        diet.setLastModify(LocalDateTime.now(ZoneId.of(DEFAULT_TIMEZONE)));

        List<Recipe> recipes = new ArrayList<>();
        for (UUID recipeId : dietRequest.getRecipes()) {
            recipes.add(recipeService.getRecipe(recipeId));
        }
        diet.setRecipes(recipes);
    }

    @Transactional
    public void saveDiet(@NotNull @Valid DietRequest dietRequest, @NotNull UUID userId) {
        Diet diet = new Diet();
        diet.setCreatedAt(LocalDateTime.now(ZoneId.of(DEFAULT_TIMEZONE)));
        modifyDiet(dietRequest, userId, diet);
        dietRepository.save(diet);
    }

    @Transactional
    public void updateDiet(@NotNull @Valid DietRequest dietRequest, @NotNull UUID id, @NotNull UUID userId) {
        Diet diet = dietRepository.findByIdAndUserId(id, userId);
        if (diet == null) throw new UnauthorizedException("You don't have permission to update this diet");
        modifyDiet(dietRequest, userId, diet);
        dietRepository.save(diet);
    }

    @Transactional
    public void deleteDiet(@NotNull UUID id, @NotNull UUID userId) {
        if (dietRepository.findByIdAndUserId(id, userId) == null) throw new UnauthorizedException("You don't have permission to delete this diet");
        dietRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<UserDietView> getPublicUserDiets(UUID id) {
        return dietRepository.findPublicByUserId(id);
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
