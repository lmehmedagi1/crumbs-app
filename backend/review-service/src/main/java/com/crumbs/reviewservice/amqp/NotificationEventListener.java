package com.crumbs.reviewservice.amqp;

import com.crumbs.reviewservice.repositories.ReviewRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class NotificationEventListener {

    private final ReviewRepository reviewRepository;

    @Autowired
    public NotificationEventListener(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @RabbitListener(queues = "NOTIFICATION_QUEUE")
    public void handleNotificationFailedEvent(NotificationFailedEvent event) {
        try {
            reviewRepository.deleteById(event.getReviewId());
            System.out.println("Handling a notification failed event " + event);
            log.info("Handling a notification failed event {}", event);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
