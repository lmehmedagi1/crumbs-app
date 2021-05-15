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
    private final String uuid_regex = "^[0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12}$";

    @NotBlank
    @Size(max = 150, message = "Description exceeds allowed limit of 250 characters!")
    @Pattern(regexp = "^[A-Za-z0-9 .,:;\\-_?!&%/'@()\"]*$", flags = {Pattern.Flag.MULTILINE, Pattern.Flag.UNICODE_CASE},
            message = "Description can only contain letters, numbers, spaces, and punctuation marks!")
    private String description;

    @NotNull
    private Boolean is_read;
}