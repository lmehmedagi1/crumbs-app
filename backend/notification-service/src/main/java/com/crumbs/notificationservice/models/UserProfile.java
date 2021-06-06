package com.crumbs.notificationservice.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    private UUID id;

    @JsonProperty("first_name")
    @NotBlank
    @Pattern(regexp = "^[A-Za-zščćžđŠČĆŽĐ]+$", flags = Pattern.Flag.UNICODE_CASE, message = "First name can only contain letters!")
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank
    @Pattern(regexp = "^[A-Za-zščćžđŠČĆŽĐ]+$", flags = Pattern.Flag.UNICODE_CASE, message = "Last name can only contain letters!")
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^(male|female|other)$", message = "Gender can only be male, female or other!")
    private String gender;

    @JsonProperty("phone_number")
    @NotBlank
    @Size(min = 3, max = 15, message = "Phone number must be between 3 and 15 numbers!")
    @Pattern(regexp = "^[0-9]+$", message = "Invalid phone number format!")
    private String phoneNumber;

    @JsonBackReference
    @NotNull
    private User user;

    private String avatar;
}
