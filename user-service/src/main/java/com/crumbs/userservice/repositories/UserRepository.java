package com.crumbs.userservice.repositories;

import com.crumbs.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByFirstName(String name);
}
