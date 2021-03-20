package com.crumbs.reviewservice.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ReviewEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ReviewNotFoundException.class)
    protected ResponseEntity<ApiError> handleNoHandlerFoundException(ReviewNotFoundException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError exceptionResponse = new ApiError(LocalDateTime.now(), status, exception.getMessage(),
                status.getReasonPhrase(), ((ServletWebRequest) request).getRequest().getRequestURI());

        return new ResponseEntity<>(exceptionResponse, status);
    }
}