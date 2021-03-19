package com.crumbs.userservice.responses;

import com.crumbs.userservice.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class LoginAndRegistrationResponse {

    @NonNull
    private final User user;

    @NonNull
    private final String jwt;
}
