package com.crumbs.userservice.exceptions;

import com.crumbs.userservice.utility.apierror.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class UserEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private String getRequestUri(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }

    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST, "Missing request parameter",
                "Parameter " + ex.getParameterName() + " must not be empty!");
        apiError.setPath(getRequestUri(request));
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        String error = "Specified media type is not supported";
        StringBuilder message = new StringBuilder();
        message.append("Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> message.append(t).append(", "));
        ApiError apiError = new ApiError(UNSUPPORTED_MEDIA_TYPE, error.substring(0, error.length() - 2),
                message.toString(), getRequestUri(request));
        return new ResponseEntity<>(apiError, UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST, "Validation error");
        apiError.setPath(getRequestUri(request));
        apiError.addApiSubError(ex.getBindingResult().getFieldErrors());
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex, WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST, "Validation error");
        apiError.setPath(getRequestUri(request));
        apiError.addApiSubError(ex.getConstraintViolations());
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    /**
     * Handles UserNotFoundException.
     * Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     */
    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            UserNotFoundException ex, WebRequest request) {
        ApiError apiError = new ApiError(NOT_FOUND, ex.getMessage(), ex.getAltMessage(), getRequestUri(request));
        return new ResponseEntity<>(apiError, NOT_FOUND);
    }

    /**
     * Handles UserAlreadyExistsException
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            UserAlreadyExistsException ex, WebRequest request) {
        ApiError apiError = new ApiError(CONFLICT, ex.getMessage(), ex.getAltMessage(), getRequestUri(request));
        return new ResponseEntity<>(apiError, CONFLICT);
    }

    /**
     * Handles IncorrectPasswordException
     */
    @ExceptionHandler(IncorrectPasswordException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            IncorrectPasswordException ex, WebRequest request) {
        ApiError apiError = new ApiError(UNAUTHORIZED, ex.getMessage(), "Check your password and try again!", getRequestUri(request));
        return new ResponseEntity<>(apiError, UNAUTHORIZED);
    }

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST, "Malformed JSON request",
                "Check your JSON request and try again!", getRequestUri(request));
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    /**
     * Handle HttpMessageNotWritableException.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR, "Conversion failed", "Error writing JSON output!");
        apiError.setPath(getRequestUri(request));
        return new ResponseEntity<>(apiError, INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle NoHandlerFoundException.
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST, "No handler found", String.format("Could not find the %s method for URL %s!",
                ex.getHttpMethod(), ex.getRequestURL()), getRequestUri(request));
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    /**
     * Handle javax.persistence.EntityNotFoundException
     */
    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex, WebRequest request) {
        ApiError apiError = new ApiError(NOT_FOUND, "Database error", "Entity does not exist!", getRequestUri(request));
        return new ResponseEntity<>(apiError, NOT_FOUND);
    }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        ApiError apiError = new ApiError(CONFLICT, "Database error", "Data integrity violation occurred!",
                getRequestUri(request));
        if (ex.getCause() instanceof ConstraintViolationException)
            return new ResponseEntity<>(apiError, CONFLICT);

        apiError.setStatus(INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiError, INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle Exception, handle generic Exception.class
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setError("Method argument has not the expected type");
        apiError.setMessage(String.format("The parameter '%s' of value '%s' is of invalid format!",
                ex.getName(), ex.getValue()));
        apiError.setPath(getRequestUri(request));
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }
}