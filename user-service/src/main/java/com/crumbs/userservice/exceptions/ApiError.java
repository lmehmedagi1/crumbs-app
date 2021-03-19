package com.crumbs.userservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
class ApiError {

    private Date timestamp;
    private int status;
    private String message;
    private String error;
    private String path;

    // constuctor, setters, getters...
}
