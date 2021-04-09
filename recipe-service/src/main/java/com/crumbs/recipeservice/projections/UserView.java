package com.crumbs.recipeservice.projections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserView {

    public UserView(UUID userId) {
        this.userId = userId;
    }

    private UUID userId;
    private String username;
    private byte[] avatar;
}
