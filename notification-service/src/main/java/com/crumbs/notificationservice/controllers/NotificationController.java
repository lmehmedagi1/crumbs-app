package com.crumbs.notificationservice.controllers;

import com.crumbs.notificationservice.exceptions.NotificationNotFoundException;
import com.crumbs.notificationservice.models.Notification;
import com.crumbs.notificationservice.requests.NotificationRequest;
import com.crumbs.notificationservice.services.NotificationService;
import com.crumbs.notificationservice.utility.NotificationModelAssembler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/notifications")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
public class NotificationController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final NotificationService notificationService;
    private final NotificationModelAssembler notificationModelAssembler;

    @Autowired
    NotificationController(NotificationService notificationService, NotificationModelAssembler notificationModelAssembler) {
        this.notificationService = notificationService;
        this.notificationModelAssembler = notificationModelAssembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Notification>> getAllNotifications() {
        List<EntityModel<Notification>> notifications = notificationService.getAllNotifications().stream()
                .map(notificationModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(notifications, linkTo(methodOn(NotificationController.class).getAllNotifications()).withSelfRel());
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public EntityModel<Notification> getNotificationById(@RequestParam("id") @NotNull UUID id) {
        return notificationModelAssembler.toModel(notificationService.getNotification(id));
    }

    @RequestMapping(params = "userId", method = RequestMethod.GET)
    public CollectionModel<EntityModel<Notification>> getNotificationsOfUser(@RequestParam("userId") @NotNull UUID userId) {
        List<EntityModel<Notification>> reviews = notificationService.getNotificationsOfUser(userId).stream()
                .map(notificationModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(reviews, linkTo(methodOn(NotificationController.class).getAllNotifications()).withSelfRel());
    }

    @PostMapping
    public ResponseEntity<?> createNotification(@RequestBody @Valid NotificationRequest notificationRequest) throws NotificationNotFoundException {
        final Notification newNotification = notificationService.saveNotification(notificationRequest);
        EntityModel<Notification> entityModel = notificationModelAssembler.toModel(newNotification);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PatchMapping(consumes = "application/json")
    public ResponseEntity<?> updateNotification(@RequestParam("id") @NotNull UUID id, @RequestBody @Valid NotificationRequest notificationRequest) {
        final Notification updatedNotification = notificationService.updateNotification(notificationRequest, id);
        EntityModel<Notification> entityModel = notificationModelAssembler.toModel(updatedNotification);
        return ResponseEntity.ok().body(entityModel);
    }

    /**
     * PATCH method with partial update, based on JSON Patch
     */
    @PatchMapping(consumes = "application/json-patch+json")
    public ResponseEntity<?> patchNotification(@RequestParam("id") @NotNull UUID id, @RequestBody JsonPatch patch) {
        try {
            Notification notification = notificationService.getNotification(id);
            JsonNode patched = patch.apply(objectMapper.convertValue(notification, JsonNode.class));
            Notification notificationPatched = objectMapper.treeToValue(patched, Notification.class);
            notificationService.updateNotification(notificationPatched);
            return ResponseEntity.ok(notificationPatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteNotification(@RequestParam("id") @NotNull UUID id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}