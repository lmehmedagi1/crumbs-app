package com.crumbs.notificationservice.utility;

import com.crumbs.notificationservice.controllers.NotificationController;
import com.crumbs.notificationservice.models.Notification;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class NotificationModelAssembler implements RepresentationModelAssembler<Notification, EntityModel<Notification>> {

    @Override
    public EntityModel<Notification> toModel(Notification notification) {
        return EntityModel.of(notification, linkTo(methodOn(NotificationController.class).getNotification(notification.getId().toString())).withSelfRel(),
                linkTo(methodOn(NotificationController.class).getAllNotifications()).withRel("notifications"));
    }

    @Override
    public CollectionModel<EntityModel<Notification>> toCollectionModel(Iterable<? extends Notification> entities) {
        return null;
    }
}
