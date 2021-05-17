package com.crumbs.notificationservice.amqp;

import com.crumbs.notificationservice.requests.NotificationRequest;
import com.crumbs.notificationservice.services.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ReviewEventListener {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "REVIEW_QUEUE")
    public void handleReviewEvent(ReviewCreatedEvent event) {
        try {
            NotificationRequest notificationRequest = new NotificationRequest("Notification RabbitMQ", false);
            if (event.getPoruka().equals("FailOnPurpose"))
                throw new RuntimeException("Intentional");
            notificationService.saveNotification(notificationRequest, UUID.randomUUID());
        } catch (Exception e) {
            NotificationFailedEvent failedEvent = new NotificationFailedEvent(UUID.randomUUID().toString(), event.getReviewId());
            rabbitTemplate.convertAndSend("NOTIFICATION_EXCHANGE", "NOTIFICATION_ROUTING_KEY", failedEvent);
        }
    }
}
