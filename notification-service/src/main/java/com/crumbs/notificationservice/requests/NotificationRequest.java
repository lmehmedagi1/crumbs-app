package com.crumbs.notificationservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class NotificationRequest {
    // UUID must be VERSION 4 !!
    @NotBlank
    @Pattern(regexp = "^[0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12}$",
            flags = Pattern.Flag.CASE_INSENSITIVE, message = "UUID string format is invalid!")
    private String user_id;

    @NotBlank
    @Size(max = 150, message = "Description exceeds allowed limit of 250 characters!")
    @Pattern(regexp = "^[A-Za-z0-9 _.,!?\"'-/]*$", flags = Pattern.Flag.UNICODE_CASE,
            message = "Description can only contain letters, numbers, spaces, and punctuation.")
    private String description;

    @NotNull
    private Boolean is_read;
}
