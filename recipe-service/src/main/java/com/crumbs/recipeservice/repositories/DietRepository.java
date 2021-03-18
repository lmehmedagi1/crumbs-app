package com.crumbs.recipeservice.repositories;

import com.crumbs.recipeservice.models.Diet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DietRepository extends JpaRepository<Diet, UUID> {
}
