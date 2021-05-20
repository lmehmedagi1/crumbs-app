package com.crumbs.userservice.repositories;

import com.crumbs.userservice.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    RefreshToken findByValue(String value);
    RefreshToken findByUser_Id(UUID id);
}
