package com.crumbs.userservice.requests;

import com.crumbs.userservice.utility.annotation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank
    @Size(min = 6, max = 30, message = "Username must be between 6 and 30 characters!")
    @Pattern(regexp = "^(?!.*\\.\\.)(?!.*\\.$)[a-z0-9_.]*$", message = "Username can only have lowercase " +
            "letters, numbers and underscores!")
    private String username;

    @NotBlank
    @ValidEmail
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~])(?=\\S+$).{8,}$", message = "Password must be at least 8 characters long." +
            "There must be at least one digit, one lowercase and one uppercase letter, one special character and no whitespaces!")
    private String password;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z]+$", flags = Pattern.Flag.UNICODE_CASE, message = "First name can only contain letters!")
    private String first_name;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z]+$", flags = Pattern.Flag.UNICODE_CASE, message = "Last name can only contain letters!")
    private String last_name;

    @NotBlank
    @Pattern(regexp = "^(male|female|other)$", message = "Gender can only be male, female or other!")
    private String gender;

    @NotBlank
    @Size(min = 3, max = 15, message = "Phone number must be between 3 and 15 numbers!")
    @Pattern(regexp = "^[0-9]+$", message = "Invalid phone number format!")
    private String phone_number;
}
