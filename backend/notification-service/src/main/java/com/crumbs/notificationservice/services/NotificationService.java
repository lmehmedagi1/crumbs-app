package com.crumbs.notificationservice.services;

import com.crumbs.notificationservice.exceptions.NotificationNotFoundException;
import com.crumbs.notificationservice.exceptions.UnauthorizedException;
import com.crumbs.notificationservice.models.Notification;
import com.crumbs.notificationservice.repositories.NotificationRepository;
import com.crumbs.notificationservice.responses.NotificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Service
@Validated
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Transactional(readOnly = true)
    public List<Notification> getNotificationsOfUser(@NotNull UUID userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public void deleteNotification(@NotNull UUID id, @NotNull UUID userId) {
        if (notificationRepository.findByIdAndUserId(id, userId) == null)
            throw new NotificationNotFoundException("You don't have permission to delete this review");
        notificationRepository.deleteById(id);
    }

    @Transactional
    public void markNotification(@NotNull UUID id, @NotNull UUID userId) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification == null) throw new NotificationNotFoundException();
        if (!notification.getUserId().equals(userId)) throw new UnauthorizedException();
        notification.setIsRead(!notification.getIsRead());
        notificationRepository.save(notification);
    }

    @Transactional
    public int markAllAsReadForUser(@NotNull UUID userId) {
        return notificationRepository.markAllAsRead(userId);
    }

    @Transactional
    public void sendNotification(@NotNull Notification notification) {
        notificationRepository.save(notification);
        simpMessagingTemplate.convertAndSend("/topic/" + notification.getUserId().toString(), new NotificationResponse(notification));
    }
}
