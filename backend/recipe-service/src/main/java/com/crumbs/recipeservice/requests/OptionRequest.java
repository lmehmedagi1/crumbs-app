package com.crumbs.recipeservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionRequest {
    @Pattern(regexp = "^[A-Za-z\\s]+$", flags = Pattern.Flag.UNICODE_CASE, message = "Type can only contain letters and whitespaces!")
    public String type;

    public String searchTerm;
}
