package com.crumbs.notificationservice.repositories;

import com.crumbs.notificationservice.models.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    Slice<Notification> findByUserId(UUID uuid, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE Notification n SET n.isRead = true WHERE n.userId = ?1")
    int markAllAsRead(UUID userId);
}