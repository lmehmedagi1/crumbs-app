package com.crumbs.recipeservice.repositories;

import com.crumbs.recipeservice.models.Diet;
import com.crumbs.recipeservice.models.Recipe;
import com.crumbs.recipeservice.projections.UserDietView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DietRepository extends JpaRepository<Diet, UUID> {

    Diet findByIdAndUserId(UUID id, UUID userId);

    @Query("SELECT new com.crumbs.recipeservice.projections.UserDietView(r.id, r.title, r.description, 0., '') " +
            "FROM Diet r WHERE r.userId=?1")
    List<UserDietView> findByUserId(UUID id);

    @Query("SELECT d.recipes FROM Diet d WHERE d.id=?1")
    List<Recipe> getDietRecipes(UUID id);
}
