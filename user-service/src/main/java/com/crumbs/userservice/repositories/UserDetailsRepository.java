package com.crumbs.userservice.repositories;

import com.crumbs.userservice.models.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserDetailsRepository extends JpaRepository<UserDetails, UUID> {
}
