package com.crumbs.recipeservice.projections;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserClassView {
    private UUID id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String avatar;

    public UserClassView(UUID userId) {
        this.id = userId;
    }
}

