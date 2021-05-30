package com.crumbs.recipeservice.repositories;

import com.crumbs.recipeservice.models.Category;
import com.crumbs.recipeservice.projections.CategoryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query("SELECT new com.crumbs.recipeservice.projections.CategoryView(c.id, c.name, ct.id, ct.name) " +
            "FROM Category c " +
            "INNER JOIN CategoryType ct on ct.id = c.categoryType " +
            "WHERE ct.name = ?1 AND (?2 IS NULL OR ?2 = '''' OR c.name LIKE %?2%)")
    List<CategoryView> findCategoriesByType(String name, String search);
}
