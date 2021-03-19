package com.crumbs.userservice.requests;

import com.crumbs.userservice.utilities.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank
    @NonNull
    private String firstName;

    @NotBlank
    @NonNull
    private String lastName;

    @NotBlank
    @NonNull
    private String username;

    @NotBlank
    @NonNull
    @ValidEmail
    private String email;

    @NotBlank
    @NonNull
    private String password;

    @NotBlank
    @NonNull
    private String gender;

    private String phoneNumber;
}
