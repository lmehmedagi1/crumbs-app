package com.crumbs.userservice.requests;

import com.crumbs.userservice.utility.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank
    @NonNull
    @Pattern(regexp = "^[A-Za-z]+$", flags = Pattern.Flag.UNICODE_CASE, message = "First name can only contain letters.")
    private String firstName;

    @NotBlank
    @NonNull
    @Pattern(regexp = "^[A-Za-z]+$", flags = Pattern.Flag.UNICODE_CASE, message = "Last name can only contain letters.")
    private String lastName;

    @NotBlank
    @NonNull
    @Size(min = 6, max = 30, message= "Username must be between 6 and 30 characters")
    @Pattern(regexp = "^(?!.*\\.\\.)(?!.*\\.$)[a-z0-9_.]{6,29}$", message = "Username can only have lowercase " +
            "letters, numbers and underscores.")
    private String username;

    @NotBlank
    @NonNull
    @ValidEmail
    private String email;

    @NotBlank
    @NonNull
    @Size(min = 8, message= "Password must contain at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", message = "Password must be minimum " +
            "eight characters, at least one uppercase letter, one lowercase letter and one number")
    private String password;

    @NotBlank
    @NonNull
    @Pattern(regexp = "^(male|female|other)$", message = "Gender can only be male, female or other.")
    private String gender;

    @Size(min = 3, max = 15, message= "Phone number must be between 3 and 15 numbers")
    @Pattern(regexp = "^[0-9]+$", message = "Invalid phone number format.")
    private String phoneNumber;
}
