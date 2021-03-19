package com.crumbs.userservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class LoginRequest {

    @NotBlank
    @NonNull
    private String username;

    @NotBlank
    @NonNull
    private String password;
}
