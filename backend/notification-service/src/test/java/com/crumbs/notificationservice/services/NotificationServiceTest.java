package com.crumbs.notificationservice.services;

import com.crumbs.notificationservice.exceptions.NotificationNotFoundException;
import com.crumbs.notificationservice.models.Notification;
import com.crumbs.notificationservice.requests.NotificationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;
//
//    @Test
//    void testGetNotificationIncorrectId() {
//        assertThrows(NotificationNotFoundException.class, () -> notificationService.getNotification(UUID.fromString("fb244360-88cb-11eb-8dcd-0242ac130005")));
//    }
//
//    @Test
//    void testGetNotificationNull() {
//        assertThrows(InvalidDataAccessApiUsageException.class, () -> notificationService.getNotification(null));
//    }
//
//    @Test
//    void testGetNotificationPagination() {
//        final List<Notification> notifications = notificationService.getNotificationsOfUser(UUID.fromString("9c76205a-afa0-4699-9ec3-0ce0f10c515e"), 0, 3, "createdAt");
//        assertEquals(3, notifications.size());
//    }
//
//    @Test
//    void testGetNotificationCorrectId() {
//        final Notification notification = notificationService.getNotification(UUID.fromString("2eb4c3bb-569a-4af7-b763-5dab250d8bfe"));
//        assertAll(() -> assertEquals("Lajk od usera 2", notification.getDescription()),
//                () -> assertEquals(false, notification.getIsRead()));
//    }
//
//    @Test
//    void testCreateNotificationNullInputParameter() {
//        assertThrows(NullPointerException.class, () -> {
//            notificationService.saveNotification(null);
//        });
//    }
//
//    @Test
//    void testCreateNotificationSuccess() {
//        final NotificationRequest createRecipeRequest = new NotificationRequest("6494e288-f6ac-47b1-b6d6-c52724c35570", "Test Notification Description", true);
//        final Notification notification = notificationService.saveNotification(createRecipeRequest);
//        assertAll(() -> assertEquals("Test Notification Description", notification.getDescription()),
//                () -> assertEquals(true, notification.getIsRead()));
//    }

//    @Test
//    void testDeleteNotificationSuccess() {
//        notificationService.deleteNotification(UUID.fromString("2eb4c3bb-569a-4af7-b763-5dab250d8bfe"));
//        assertThrows(NotificationNotFoundException.class, () -> notificationService.getNotification(UUID.fromString("2eb4c3bb-569a-4af7-b763-5dab250d8bfe")));
//    }

    @Test
    void testUpdateMarkAllAsReadNotificationSuccess() {
        int value = notificationService.markAllAsReadForUser(UUID.fromString("9c76205a-afa0-4699-9ec3-0ce0f10c515e"));
        assertEquals(4, value);
    }

}