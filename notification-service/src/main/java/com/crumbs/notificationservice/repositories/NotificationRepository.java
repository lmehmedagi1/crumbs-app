package com.crumbs.notificationservice.repositories;

import com.crumbs.notificationservice.models.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    Slice<Notification> findByUserId(UUID uuid, Pageable pageable);

    @Modifying
    @Query("update Notification n set n.isRead = true where n.userId = ?1")
    int markAllAsRead(UUID userId);

    //List<Recepie> findBySastojak_IdIn(UUID)


}