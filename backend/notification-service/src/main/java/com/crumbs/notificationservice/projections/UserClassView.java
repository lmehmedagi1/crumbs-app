package com.crumbs.notificationservice.projections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserClassView {
    private UUID id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String avatar;
}
