package com.crumbs.recipeservice.payroll;

import com.crumbs.recipeservice.exceptions.ApiError;
import com.crumbs.recipeservice.exceptions.IngredientNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
class IngredientNotFoundAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IngredientNotFoundException.class)
    public final ResponseEntity<ApiError> handleNotFoundException(IngredientNotFoundException ex, WebRequest request) {
        ApiError exceptionResponse = new ApiError(new Date(), HttpStatus.NOT_FOUND.value(), ex.getMessage(),
                HttpStatus.NOT_FOUND.getReasonPhrase(), ((ServletWebRequest) request).getRequest().getRequestURI());

        return new ResponseEntity<ApiError>(exceptionResponse, HttpStatus.NOT_FOUND);
    }
}