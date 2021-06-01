package com.crumbs.notificationservice.amqp;

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
    private UUID userId;
    private String poruka;
}
