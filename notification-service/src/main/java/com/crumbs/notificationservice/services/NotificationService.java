package com.crumbs.notificationservice.services;

import com.crumbs.notificationservice.exceptions.NotificationNotFoundException;
import com.crumbs.notificationservice.models.Notification;
import com.crumbs.notificationservice.repositories.NotificationRepository;
import com.crumbs.notificationservice.requests.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional(readOnly = true)
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Notification getNotification(@NotBlank String id) throws NotificationNotFoundException {
        return notificationRepository.findById(UUID.fromString(id)).orElseThrow(() ->
                new NotificationNotFoundException(Notification.class, "id", id));
    }

    private void modifyNotification(NotificationRequest notificationRequest, Notification notification) {
        notification.setUserId(UUID.fromString(notificationRequest.getUser_id()));
        notification.setIsRead(notificationRequest.getIs_read());
        notification.setDescription(notificationRequest.getDescription());
    }

    @Transactional
    public Notification saveNotification(@NotNull @Valid NotificationRequest notificationRequest) {
        Notification notification = new Notification();
        modifyNotification(notificationRequest, notification);
        return notificationRepository.save(notification);
    }

    @Transactional
    public Notification updateNotification(@NotNull @Valid NotificationRequest notificationRequest, @NotBlank String id) {
        return notificationRepository.findById(UUID.fromString(id)).map(notification -> {
            modifyNotification(notificationRequest, notification);
            return notificationRepository.save(notification);
        }).orElseThrow(() -> new NotificationNotFoundException(Notification.class, "id", id));
    }

    @Transactional
    public void updateNotification(@NotNull @Valid Notification notification) {
        notificationRepository.save(notification);
    }

    @Transactional
    public void deleteNotification(@NotBlank String id) {
        if (!notificationRepository.existsById(UUID.fromString(id)))
            throw new NotificationNotFoundException(Notification.class, "id", id);

        notificationRepository.deleteById(UUID.fromString(id));
    }
}
