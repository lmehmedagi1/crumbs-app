package com.crumbs.userservice.repositories;

import com.crumbs.userservice.models.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {
    VerificationToken findByValue(String value);
}
