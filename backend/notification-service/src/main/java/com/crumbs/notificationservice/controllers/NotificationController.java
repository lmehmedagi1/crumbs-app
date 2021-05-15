package com.crumbs.notificationservice.controllers;

import com.crumbs.notificationservice.models.Notification;
import com.crumbs.notificationservice.requests.NotificationRequest;
import com.crumbs.notificationservice.requests.NotificationWebClientRequest;
import com.crumbs.notificationservice.services.NotificationService;
import com.crumbs.notificationservice.utility.assemblers.NotificationModelAssembler;
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

    private final NotificationService notificationService;
    private final NotificationModelAssembler notificationModelAssembler;
    private final NotificationWebClientRequest notificationWebClientRequest;

    @Autowired
    NotificationController(NotificationService notificationService, NotificationModelAssembler notificationModelAssembler, NotificationWebClientRequest notificationWebClientRequest) {
        this.notificationService = notificationService;
        this.notificationModelAssembler = notificationModelAssembler;
        this.notificationWebClientRequest = notificationWebClientRequest;
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public EntityModel<Notification> getNotificationById(@RequestParam("id") @NotNull UUID id) {
        return notificationModelAssembler.toModel(notificationService.getNotification(id));
    }

    @RequestMapping(params = "userId", method = RequestMethod.GET)
    public CollectionModel<EntityModel<Notification>> getNotificationsForUser(@RequestParam("userId") @NotNull UUID userId,
                                                                              @RequestParam(defaultValue = "0") Integer pageNo,
                                                                              @RequestParam(defaultValue = "3") Integer pageSize,
                                                                              @RequestParam(defaultValue = "createdAt") String sortBy) {
        notificationWebClientRequest.checkIfUserExists(userId);
        List<EntityModel<Notification>> reviews = notificationService.getNotificationsOfUser(userId, pageNo, pageSize, sortBy)
                .stream()
                .map(notificationModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(reviews, linkTo(methodOn(NotificationController.class)
                .getNotificationsForUser(userId, pageNo, pageSize, sortBy)).withSelfRel());
    }

    @PostMapping
    public ResponseEntity<?> createNotification(@RequestBody @Valid NotificationRequest notificationRequest) {
        notificationWebClientRequest.checkIfUserExists(UUID.fromString(notificationRequest.getUser_id()));
        final Notification newNotification = notificationService.saveNotification(notificationRequest);
        EntityModel<Notification> entityModel = notificationModelAssembler.toModel(newNotification);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @RequestMapping(value = "/markAsRead")
    public int markAllAsReadForUser(@RequestParam("userId") @NotNull UUID id) {
        notificationWebClientRequest.checkIfUserExists(id);
        return notificationService.markAllAsReadForUser(id);
    }

    @PatchMapping(consumes = "application/json")
    public ResponseEntity<?> updateNotification(@RequestParam("id") @NotNull UUID id,
                                                @RequestBody @Valid NotificationRequest notificationRequest) {
        final Notification updatedNotification = notificationService.updateNotification(notificationRequest, id);
        return ResponseEntity.ok().body(notificationModelAssembler.toModel(updatedNotification));
    }

    /**
     * PATCH method with partial update, based on JSON Patch
     */
    @PatchMapping(consumes = "application/json-patch+json")
    public ResponseEntity<?> patchNotification(@RequestParam("id") @NotNull UUID id,
                                               @RequestBody JsonPatch patch) {
        try {
            Notification notification = notificationService.getNotification(id);
            final ObjectMapper objectMapper = new ObjectMapper();
            JsonNode patched = patch.apply(objectMapper.convertValue(notification, JsonNode.class));
            Notification notificationPatched = objectMapper.treeToValue(patched, Notification.class);
            notificationService.updateNotification(notificationPatched);
            return ResponseEntity.ok(notificationModelAssembler.toModel(notification));
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteNotification(@RequestParam("id") @NotNull UUID id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}