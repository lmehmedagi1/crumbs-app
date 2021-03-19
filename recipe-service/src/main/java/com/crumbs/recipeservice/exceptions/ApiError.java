package com.crumbs.recipeservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    private Date timestamp;
    private int status;
    private String message;
    private String error;
    private String path;
}
