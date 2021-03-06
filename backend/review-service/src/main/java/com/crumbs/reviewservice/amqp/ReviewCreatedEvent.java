package com.crumbs.reviewservice.amqp;

import com.crumbs.reviewservice.models.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
