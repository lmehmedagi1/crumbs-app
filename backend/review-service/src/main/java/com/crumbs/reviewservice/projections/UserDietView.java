package com.crumbs.reviewservice.projections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDietView {
    private UUID id;
    private String title;
    private String description;
    private double rating;
}
