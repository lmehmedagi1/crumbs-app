package com.crumbs.recipeservice.exceptions;

import com.crumbs.recipeservice.utility.apierror.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
class RecipeAndDietExceptionHandler extends ResponseEntityExceptionHandler {

    private String getRequestUri(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatus status,
                                                                         WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST, "Request method not supported",
                "Check the request and try again!", getRequestUri(request));
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<Object> handleIllegalStateException(IllegalStateException exception, WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST, "Requested operation caused an inappropriate state",
                "Check the request and try again!", getRequestUri(request));
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST, "Missing request parameter",
                "Parameter " + ex.getParameterName() + " must be specified!");
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
     * Handle MethodArgumentNotValidException.
     * Triggered when an object fails @Valid validation.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        String message = null;
        try {
            message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        } catch (Exception ignored) {
        }
        ApiError apiError = new ApiError(BAD_REQUEST, "Validation error", message, getRequestUri(request));
        apiError.addApiSubError(ex.getBindingResult().getFieldErrors());
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    /**
     * Handles javax.validation.ConstraintViolationException.
     * Triggered when an object fails @Validated validation.
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex, WebRequest request) {
        String message = null;
        try {
            message = ex.getConstraintViolations().iterator().next().getMessage();
        } catch (Exception ignored) {
        }
        ApiError apiError = new ApiError(BAD_REQUEST, "Validation error", message, getRequestUri(request));
        apiError.addApiSubError(ex.getConstraintViolations());
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    /**
     * Handles CategoryNotFoundException.
     * Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     */
    @ExceptionHandler(CategoryNotFoundException.class)
    protected ResponseEntity<Object> handleCategoryNotFound(
            CategoryNotFoundException ex, WebRequest request) {
        ApiError apiError = new ApiError(NOT_FOUND, ex.getMessage(), "Category with specified parameters does not exist!", getRequestUri(request));
        return new ResponseEntity<>(apiError, NOT_FOUND);
    }

    /**
     * Handles IngredientNotFoundException.
     * Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     */
    @ExceptionHandler(IngredientNotFoundException.class)
    protected ResponseEntity<Object> handleIngredientNotFound(
            IngredientNotFoundException ex, WebRequest request) {
        ApiError apiError = new ApiError(NOT_FOUND, ex.getMessage(), "Ingredient with specified parameters does not exist!", getRequestUri(request));
        return new ResponseEntity<>(apiError, NOT_FOUND);
    }

    /**
     * Handles RecipeNotFoundException.
     * Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     */
    @ExceptionHandler(RecipeNotFoundException.class)
    protected ResponseEntity<Object> handleRecipeNotFound(
            RecipeNotFoundException ex, WebRequest request) {
        ApiError apiError = new ApiError(NOT_FOUND, ex.getMessage(), "Recipe with specified parameters does not exist!", getRequestUri(request));
        return new ResponseEntity<>(apiError, NOT_FOUND);
    }

    /**
     * Handles DietNotFoundException.
     * Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     */
    @ExceptionHandler(DietNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            DietNotFoundException ex, WebRequest request) {
        ApiError apiError = new ApiError(NOT_FOUND, ex.getMessage(), "Diet with specified parameters does not exist!", getRequestUri(request));
        return new ResponseEntity<>(apiError, NOT_FOUND);
    }

    /**
     * Handle HttpMessageNotReadableException.
     * Happens when request JSON is malformed.
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
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR, "Conversion failure", "Error writing JSON output!");
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