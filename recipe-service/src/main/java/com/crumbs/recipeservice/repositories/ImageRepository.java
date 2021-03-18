package com.crumbs.recipeservice.repositories;

import com.crumbs.recipeservice.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
}
