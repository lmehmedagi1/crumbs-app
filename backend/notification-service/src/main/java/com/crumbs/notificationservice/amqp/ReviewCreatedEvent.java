package com.crumbs.notificationservice.amqp;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReviewCreatedEvent {
    private String transactionId;
    private UUID reviewId;
    private UUID recipeId;
    private UUID authorId;
    private UUID reviewerId;
    private String message;
}
