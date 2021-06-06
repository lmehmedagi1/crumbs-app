package com.crumbs.userservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID entityId;

    public enum EntityType {recipe, diet, crumbs_user}

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "entype")
    private EntityType entityType;

    @NotBlank
    @Size(max = 100, message = "Title exceeds allowed limit of 100 characters!")
    @Pattern(regexp = "^[A-Za-z0-9ščćžđŠČĆŽĐ .,:;\\-_?!&%/'@()\"]*$", flags = {Pattern.Flag.MULTILINE, Pattern.Flag.UNICODE_CASE},
            message = "Title can only contain letters, numbers, spaces, and punctuation marks!")
    private String title;

    @NotBlank
    @Size(max = 150, message = "Description exceeds allowed limit of 250 characters!")
    @Pattern(regexp = "^[A-Za-z0-9ščćžđŠČĆŽĐ .,:;\\-_?!&%/'@()\"]*$", flags = {Pattern.Flag.MULTILINE, Pattern.Flag.UNICODE_CASE},
            message = "Description can only contain letters, numbers, spaces, and punctuation marks!")
    private String description;
}
