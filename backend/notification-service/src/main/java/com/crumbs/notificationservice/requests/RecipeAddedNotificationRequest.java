package com.crumbs.notificationservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeAddedNotificationRequest {
    @NotNull
    private UUID userId;

    @NotNull
    private UUID recipeId;

    @NotNull
    private String message;
}
