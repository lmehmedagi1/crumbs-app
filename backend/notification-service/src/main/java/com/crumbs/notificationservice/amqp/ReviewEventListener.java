package com.crumbs.notificationservice.amqp;

import com.crumbs.notificationservice.models.Notification;
import com.crumbs.notificationservice.services.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static com.crumbs.notificationservice.utility.Constants.DEFAULT_TIMEZONE;

@Component
public class ReviewEventListener {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "REVIEW_QUEUE")
    public void handleReviewEvent(ReviewCreatedEvent event) {
        try {
            Notification notification = new Notification();
            notification.setEntityType(Notification.EntityType.recipe);
            notification.setUserId(event.getReviewId());
            notification.setEntityId(event.getReviewId());
            notification.setTitle("New review");
            notification.setIsRead(false);
            notification.setCreatedAt(LocalDateTime.now(ZoneId.of(DEFAULT_TIMEZONE)));
            notification.setDescription(event.getPoruka());

            notificationService.sendNotification(notification);
        } catch (Exception e) {
            NotificationFailedEvent failedEvent = new NotificationFailedEvent(UUID.randomUUID().toString(), event.getReviewId());
            rabbitTemplate.convertAndSend("NOTIFICATION_EXCHANGE", "NOTIFICATION_ROUTING_KEY", failedEvent);
        }
    }
}
