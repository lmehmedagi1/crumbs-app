package com.crumbs.reviewservice.repositories;

import com.crumbs.reviewservice.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
}
