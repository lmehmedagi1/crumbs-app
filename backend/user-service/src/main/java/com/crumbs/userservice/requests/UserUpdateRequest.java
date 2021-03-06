package com.crumbs.userservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class UserUpdateRequest {
    @NotBlank
    @Pattern(regexp = "^[A-Za-zščćžđŠČĆŽĐ]+$", flags = Pattern.Flag.UNICODE_CASE, message = "First name can only contain letters!")
    private String firstName;

    @NotBlank
    @Pattern(regexp = "^[A-Za-zščćžđŠČĆŽĐ]+$", flags = Pattern.Flag.UNICODE_CASE, message = "Last name can only contain letters!")
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^(male|female|other)$", message = "Gender can only be male, female or other!")
    private String gender;

    @NotBlank
    @Size(min = 3, max = 15, message = "Phone number must be between 3 and 15 numbers!")
    @Pattern(regexp = "^[0-9]+$", message = "Invalid phone number format!")
    private String phoneNumber;

    private String email;

    private String username;

    private String avatar;
}
