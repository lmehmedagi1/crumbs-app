package com.crumbs.notificationservice.responses;

import com.crumbs.notificationservice.models.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class NotificationResponse {
    @NotNull
    private final Notification notification;
}
