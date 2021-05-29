package com.crumbs.userservice.repositories;

import com.crumbs.userservice.models.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    PasswordResetToken findByUser_Id(UUID id);
    PasswordResetToken findByValue(String value);
}
