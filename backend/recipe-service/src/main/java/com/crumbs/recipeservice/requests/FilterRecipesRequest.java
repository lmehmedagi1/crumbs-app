package com.crumbs.recipeservice.requests;

import com.crumbs.recipeservice.utility.annotation.NullOrNotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class FilterRecipesRequest {

    private String title;
    private List<UUID> categories;

}
