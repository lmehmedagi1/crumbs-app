package com.crumbs.notificationservice.services;

import com.crumbs.notificationservice.exceptions.NotificationNotFoundException;
import com.crumbs.notificationservice.models.Notification;
import com.crumbs.notificationservice.requests.NotificationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Test
    void testGetRecipeIncorrectId() {
        assertThrows(NotificationNotFoundException.class, () -> notificationService.getNotification(UUID.fromString("fb244360-88cb-11eb-8dcd-0242ac130005")));
    }

    @Test
    void testGetRecipeNull() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> notificationService.getNotification(null));
    }

    @Test
    void testGetRecipeCorrectId() {
        final Notification notification = notificationService.getNotification(UUID.fromString("2eb4c3bb-569a-4af7-b763-5dab250d8bfe"));
        assertAll(() -> assertEquals("Lajk od usera 2", notification.getDescription()),
                () -> assertEquals(false, notification.getIsRead()));
    }

    @Test
    void testCreateRecipeNullInputParameter() {
        assertThrows(NullPointerException.class, () -> {
            notificationService.saveNotification(null);
        });
    }

    @Test
    void testCreateRecipeSuccess() {
        final NotificationRequest createRecipeRequest = new NotificationRequest("6494e288-f6ac-47b1-b6d6-c52724c35570", "Test Notification Description", true);
        final Notification notification = notificationService.saveNotification(createRecipeRequest);
        assertAll(() -> assertEquals("Test Notification Description", notification.getDescription()),
                () -> assertEquals(true, notification.getIsRead()));
    }

    @Test
    void testDeleteRecipeSuccess() {
        notificationService.deleteNotification(UUID.fromString("2eb4c3bb-569a-4af7-b763-5dab250d8bfe"));
        assertThrows(NotificationNotFoundException.class, () -> notificationService.getNotification(UUID.fromString("2eb4c3bb-569a-4af7-b763-5dab250d8bfe")));
    }
}