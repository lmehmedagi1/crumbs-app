package com.crumbs.userservice.repositories;

import com.crumbs.userservice.models.Subscription;
import com.crumbs.userservice.models.SubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {
}
