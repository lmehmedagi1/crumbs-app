package com.crumbs.notificationservice.services;

import com.crumbs.notificationservice.exceptions.NotificationNotFoundException;
import com.crumbs.notificationservice.models.Notification;
import com.crumbs.notificationservice.repositories.NotificationRepository;
import com.crumbs.notificationservice.requests.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
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
    public Notification getNotification(@NotNull UUID id) throws NotificationNotFoundException {
        return notificationRepository.findById(id).orElseThrow(NotificationNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Notification> getNotificationsOfUser(@NotNull UUID userId) {
        return notificationRepository.findByUserId(userId);
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
    public Notification updateNotification(@NotNull @Valid NotificationRequest notificationRequest, @NotNull UUID id) {
        return notificationRepository.findById(id).map(notification -> {
            modifyNotification(notificationRequest, notification);
            return notificationRepository.save(notification);
        }).orElseThrow(NotificationNotFoundException::new);
    }

    @Transactional
    public void updateNotification(@NotNull @Valid Notification notification) {
        notificationRepository.save(notification);
    }

    @Transactional
    public void deleteNotification(@NotNull UUID id) {
        if (!notificationRepository.existsById(id))
            throw new NotificationNotFoundException();

        notificationRepository.deleteById(id);
    }
}