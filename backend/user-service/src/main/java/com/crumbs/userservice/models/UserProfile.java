package com.crumbs.userservice.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profile")
public class UserProfile implements Serializable {

    @Id
    @Column(name = "user_id")
    @JsonProperty("user_id")
    private UUID userId;

    @Column(name = "first_name")
    @JsonProperty("first_name")
    @NotBlank
    @Pattern(regexp = "^[A-Za-z]+$", flags = Pattern.Flag.UNICODE_CASE, message = "First name can only contain letters!")
    private String firstName;

    @Column(name = "last_name")
    @JsonProperty("last_name")
    @NotBlank
    @Pattern(regexp = "^[A-Za-z]+$", flags = Pattern.Flag.UNICODE_CASE, message = "Last name can only contain letters!")
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^(male|female|other)$", message = "Gender can only be male, female or other!")
    private String gender;

    @Column(name = "phone_number")
    @JsonProperty("phone_number")
    @NotBlank
    @Size(min = 3, max = 15, message = "Phone number must be between 3 and 15 numbers!")
    @Pattern(regexp = "^[0-9]+$", message = "Invalid phone number format!")
    private String phoneNumber;

    @NotNull
    @JsonBackReference
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private byte[] avatar;

    @Column(name = "email_verified")
    private boolean emailVerified;
}
