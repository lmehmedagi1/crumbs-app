package com.crumbs.userservice.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_details")
public class UserDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[A-Za-z]+$", flags = Pattern.Flag.UNICODE_CASE, message = "First name can only contain letters.")
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[A-Za-z]+$", flags = Pattern.Flag.UNICODE_CASE, message = "Last name can only contain letters.")
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^(male|female|other)$", message = "Gender can only be male, female or other.")
    private String gender;

    @NotNull
    @NotEmpty
    @Column(name = "phone_number")
    @Size(min = 3, max = 15, message= "Phone number must be between 3 and 15 numbers")
    @Pattern(regexp = "^[0-9]+$", message = "Invalid phone number format.")
    private String phoneNumber;

    private byte[] avatar;

    @JsonBackReference
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
