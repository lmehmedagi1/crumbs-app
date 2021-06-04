package com.crumbs.userservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {

    @NotNull
    private String token;

    @NotBlank(message = "Password must not be blank")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~])(?=\\S+$).{8,}$",
            message = "Password must be at least 8 characters long." +
            "There must be at least one digit, one lowercase and one uppercase letter, one special character and no whitespaces!")
    private String password;
}
