package com.crumbs.userservice.models;

import com.crumbs.userservice.utility.annotation.ValidEmail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotBlank
    @Size(min = 6, max = 30, message = "Username must be between 6 and 30 characters!")
    @Pattern(regexp = "^(?!.*\\.\\.)(?!.*\\.$)[a-z0-9_.]{6,29}$", flags = Pattern.Flag.UNICODE_CASE, message = "Username can only have lowercase " +
            "letters, numbers and underscores!")
    private String username;

    @NotBlank
    @ValidEmail
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password must contain at least 8 characters!")
    private String password;

    @JsonManagedReference
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @NotNull
    private UserProfile userProfile;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "subscriptions",
            joinColumns = @JoinColumn(
                    name = "subscriber_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "author_id", referencedColumnName = "id"))
    private List<User> subscriptions;
}
