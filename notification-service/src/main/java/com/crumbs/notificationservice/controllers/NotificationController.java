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
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class NotificationController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final NotificationService notificationService;
    private final NotificationModelAssembler notificationModelAssembler;

    @Autowired
    NotificationController(NotificationService notificationService, NotificationModelAssembler notificationModelAssembler) {
        this.notificationService = notificationService;
        this.notificationModelAssembler = notificationModelAssembler;
    }

    @GetMapping("/notifications")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public CollectionModel<EntityModel<Notification>> getAllNotifications() {
        List<EntityModel<Notification>> notifications = notificationService.getAllNotifications().stream()
                .map(notificationModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(notifications, linkTo(methodOn(NotificationController.class).getAllNotifications()).withSelfRel());
    }

    @GetMapping("/notifications/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public EntityModel<Notification> getNotification(@PathVariable String id) throws NotificationNotFoundException {
        return notificationModelAssembler.toModel(notificationService.getNotification(id));
    }

    @PostMapping("/notifications")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<?> createNotification(@RequestBody @Valid NotificationRequest notificationRequest) throws NotificationNotFoundException {
        final Notification newNotification = notificationService.saveNotification(notificationRequest);
        EntityModel<Notification> entityModel = notificationModelAssembler.toModel(newNotification);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PatchMapping(path = "/notifications/{id}", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<?> updateNotification(@RequestBody @Valid NotificationRequest notificationRequest, @PathVariable String id) throws NotificationNotFoundException {
        final Notification updatedNotification = notificationService.updateNotification(notificationRequest, id);
        EntityModel<Notification> entityModel = notificationModelAssembler.toModel(updatedNotification);
        return ResponseEntity.ok().body(entityModel);
    }

    private Notification applyPatchToNotification(
            JsonPatch patch, Notification targetCustomer) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(targetCustomer, JsonNode.class));
        return objectMapper.treeToValue(patched, Notification.class);
    }

    /**
     * PATCH method with partial update, based on JSON Patch
     */
    @PatchMapping(path = "/notifications/{id}", consumes = "application/json-patch+json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<?> patchNotification(@PathVariable String id, @RequestBody JsonPatch patch) {
        try {
            Notification notification = notificationService.getNotification(id);
            Notification notificationPatched = applyPatchToNotification(patch, notification);
            notificationService.updateNotification(notificationPatched);
            return ResponseEntity.ok(notificationPatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (NotificationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/notifications/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public ResponseEntity<?> deleteNotification(@PathVariable String id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

}
