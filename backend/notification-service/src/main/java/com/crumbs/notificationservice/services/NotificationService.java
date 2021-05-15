package com.crumbs.notificationservice.services;

import com.crumbs.notificationservice.exceptions.NotificationNotFoundException;
import com.crumbs.notificationservice.models.Notification;
import com.crumbs.notificationservice.repositories.NotificationRepository;
import com.crumbs.notificationservice.requests.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Service
@Validated
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional(readOnly = true)
    public Notification getNotification(@NotNull UUID id) throws NotificationNotFoundException {
//        if (notificationRepository.findByIdAndUserId(id, userId) == null)
//            throw new NotificationNotFoundException("You don't have permission to update this review");
        return notificationRepository.findById(id).orElseThrow(NotificationNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Notification> getNotificationsOfUser(@NotNull UUID userId, Integer pageNo, Integer pageSize, String sortBy) {
        Sort sorting = Sort.by(sortBy).descending();
        Pageable paging = PageRequest.of(pageNo, pageSize, sorting);
        Slice<Notification> slicedProducts = notificationRepository.findByUserId(userId, paging);
        return slicedProducts.getContent();
    }

    private void modifyNotification(NotificationRequest notificationRequest, Notification notification, UUID userId) {
        notification.setUserId(userId);
        notification.setIsRead(notificationRequest.getIs_read());
        notification.setDescription(notificationRequest.getDescription());
    }

    @Transactional
    public Notification saveNotification(@NotNull @Valid NotificationRequest notificationRequest, UUID userId) {
        Notification notification = new Notification();
        modifyNotification(notificationRequest, notification, userId);
        return notificationRepository.save(notification);
    }

    @Transactional
    public int markAllAsReadForUser(@NotNull UUID userId) {
        return notificationRepository.markAllAsRead(userId);
    }

    @Transactional
    public Notification updateNotification(@NotNull @Valid NotificationRequest notificationRequest, @NotNull UUID id, @NotNull UUID userId) {
        return notificationRepository.findById(id).map(notification -> {
            modifyNotification(notificationRequest, notification, userId);
            return notificationRepository.save(notification);
        }).orElseThrow(NotificationNotFoundException::new);
    }

    @Transactional
    public void updateNotification(@NotNull @Valid Notification notification) {
        notificationRepository.save(notification);
    }

    @Transactional
    public void deleteNotification(@NotNull UUID id, @NotNull UUID userId) {
        if (notificationRepository.findByIdAndUserId(id, userId) == null)
            throw new NotificationNotFoundException("You don't have permission to delete this review");

        notificationRepository.deleteById(id);
    }
}