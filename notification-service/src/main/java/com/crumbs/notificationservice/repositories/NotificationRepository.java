package com.crumbs.notificationservice.repositories;

import com.crumbs.notificationservice.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
}
