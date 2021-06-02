package com.crumbs.recipeservice.responses;

import com.crumbs.recipeservice.projections.DietClassView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DietViewResponse {
    private List<DietClassView> diets;
    private boolean hasNext;
}
