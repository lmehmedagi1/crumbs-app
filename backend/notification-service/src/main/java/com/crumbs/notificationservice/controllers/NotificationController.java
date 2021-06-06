package com.crumbs.notificationservice.controllers;

import com.crumbs.notificationservice.models.Notification;
import com.crumbs.notificationservice.requests.MarkNotificationRequest;
import com.crumbs.notificationservice.requests.NotificationRequest;
import com.crumbs.notificationservice.requests.NotificationWebClientRequest;
import com.crumbs.notificationservice.requests.RecipeAddedNotificationRequest;
import com.crumbs.notificationservice.services.NotificationService;
import com.crumbs.notificationservice.utility.JwtConfigAndUtil;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static com.crumbs.notificationservice.utility.Constants.DEFAULT_TIMEZONE;

@RestController
@RequestMapping(value = "/notifications")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationWebClientRequest notificationWebClientRequest;

    private UUID getUserIdFromJwt(String jwt) {
        return UUID.fromString(new JwtConfigAndUtil().extractUserId(jwt.substring(7)));
    }

    @Autowired
    NotificationController(NotificationService notificationService, NotificationWebClientRequest notificationWebClientRequest) {
        this.notificationService = notificationService;
        this.notificationWebClientRequest = notificationWebClientRequest;
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getNotificationsForUser(@RequestHeader("Authorization") String jwt) {
        UUID userId = getUserIdFromJwt(jwt);
        notificationWebClientRequest.checkIfUserExists(jwt);
        return ResponseEntity.ok(notificationService.getNotificationsOfUser(userId));
    }

    @PostMapping
    public ResponseEntity<String> createNotification(@RequestBody @Valid NotificationRequest request) {
        sendNotification(request.getDescription(), request.getTitle(), request.getUserId(), request.getEntityId(), Notification.EntityType.valueOf(request.getEntityType().toString()));
        return ResponseEntity.ok("Notification sent");
    }

    @PostMapping("/recipe")
    public ResponseEntity<String> notifySubscribers(@RequestBody @Valid RecipeAddedNotificationRequest request) {
        final UUID[] ids = notificationWebClientRequest.getUserSubscribers(request.getUserId());
        for (UUID id : ids)
            sendNotification(request.getMessage(), "New recipe", id, request.getRecipeId(), Notification.EntityType.recipe);
        return ResponseEntity.ok("Notifications sent");
    }

    @PostMapping(value = "/mark-all-as-read")
    public ResponseEntity<String> markAllAsReadForUser(@RequestHeader("Authorization") String jwt) {
        notificationWebClientRequest.checkIfUserExists(jwt);
        UUID userId = getUserIdFromJwt(jwt);
        notificationService.markAllAsReadForUser(userId);
        return ResponseEntity.ok("Notifications marked as read");
    }

    @PostMapping(value = "/mark")
    public ResponseEntity<String> markForUser(@RequestBody @Valid MarkNotificationRequest notificationRequest, @RequestHeader("Authorization") String jwt) {
        notificationWebClientRequest.checkIfUserExists(jwt);
        UUID userId = getUserIdFromJwt(jwt);
        notificationService.markNotification(notificationRequest.getId(), userId);
        return ResponseEntity.ok("Notification successfully marked");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteNotification(@RequestParam @NotNull UUID id, @RequestHeader("Authorization") String jwt) {
        UUID userId = getUserIdFromJwt(jwt);
        notificationService.deleteNotification(id, userId);
        return ResponseEntity.ok("Notification successfully deleted");
    }

    private void sendNotification(String message, String title, UUID userId, UUID entityId, Notification.EntityType type) {
        Notification notification = new Notification();
        notification.setDescription(message);
        notification.setTitle(title);
        notification.setCreatedAt(LocalDateTime.now(ZoneId.of(DEFAULT_TIMEZONE)));
        notification.setIsRead(false);
        notification.setUserId(userId);
        notification.setEntityId(entityId);
        notification.setEntityType(type);
        notificationService.sendNotification(notification);
    }
}
