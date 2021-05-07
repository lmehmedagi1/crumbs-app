package com.crumbs.gatewayservice.exception;

import com.crumbs.gatewayservice.utility.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.fasterxml.jackson.annotation.JsonFormat.DEFAULT_TIMEZONE;

@RestControllerAdvice //very advanced
public class ExceptionHandlerAdvanced extends ResponseEntityExceptionHandler {

    private ApiError getExceptionResponse(String message, HttpStatus status, WebRequest request) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        formatter.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
        Date date = new Date(System.currentTimeMillis());
        return new ApiError(formatter.format(date), status.value(), message, status.getReasonPhrase(),
                ((ServletWebRequest) request).getRequest().getRequestURI());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public final ResponseEntity<ApiError> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        ApiError apiError = getExceptionResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, request);
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

}
