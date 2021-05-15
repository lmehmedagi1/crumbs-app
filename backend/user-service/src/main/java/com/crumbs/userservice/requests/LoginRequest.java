package com.crumbs.userservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank
    @Size(min = 6, max = 30, message = "Username must be between 6 and 30 characters!")
    @Pattern(regexp = "^(?!.*\\.\\.)(?!.*\\.$)[a-z0-9_.]*$", flags = Pattern.Flag.UNICODE_CASE, message = "Username can only have lowercase " +
            "letters, numbers and underscores!")
    private String username;

    @NotBlank
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~])(?=\\S+$).{8,}$", message = "Password must be at least 8 characters long." +
            "There must be at least one digit, one lowercase and one uppercase letter, one special character and no whitespaces!")
    private String password;
}
