package com.crumbs.notificationservice.amqp;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NotificationFailedEvent {
    private String transactionId;
    private UUID reviewId;
}
