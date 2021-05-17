package com.crumbs.reviewservice.amqp;

import com.crumbs.reviewservice.services.ReviewService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class NotificationEventListener {

    private final ReviewService reviewService;

    @Autowired
    public NotificationEventListener(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @RabbitListener(queues = "NOTIFICATION_QUEUE")
    public void handleNotificationFailedEvent(NotificationFailedEvent event) {
        log.info("Handling a notification failed event {}", event);

    }
}
