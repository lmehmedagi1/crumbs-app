package com.crumbs.reviewservice.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    private UUID id;

    @NotNull
    private String image;

    @JsonBackReference
    private Recipe recipe;
}
