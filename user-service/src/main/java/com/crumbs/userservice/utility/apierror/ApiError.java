package com.crumbs.userservice.utility.apierror;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import lombok.Data;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@JsonTypeIdResolver(LowerCaseClassNameResolver.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public
class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String status;
    private Integer code;
    private String error;
    private String message;
    private String path;
    private List<ApiSubError> reason;

    public void setStatus(HttpStatus httpStatus) {
        this.status = httpStatus.getReasonPhrase();
        this.code = httpStatus.value();
    }

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus httpStatus) {
        this();
        this.setStatus(httpStatus);
    }

    public ApiError(HttpStatus status, String error) {
        this(status);
        this.error = error;
    }

    public ApiError(HttpStatus status, String error, String message) {
        this(status, error);
        this.message = message;
    }

    public ApiError(HttpStatus status, String error, String message, String path) {
        this(status, error, message);
        this.path = path;
    }

    private void addSubError(ApiSubError subError) {
        if (reason == null)
            reason = new ArrayList<>();
        reason.add(subError);
    }

    public void addApiSubError(ApiSubError apiSubError) {
        addSubError(apiSubError);
    }

    public void addApiSubError(String field, String message) {
        addSubError(new ApiSubError(field, message));
    }

    public void addApiSubError(FieldError fieldError) {
        this.addApiSubError(new ApiSubError(
                fieldError.getField(),
                fieldError.getDefaultMessage()));
    }

    public void addApiSubError(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addApiSubError);
    }

    /**
     * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
     *
     * @param cv the ConstraintViolation
     */
    private void addApiSubError(ConstraintViolation<?> cv) {
        this.addApiSubError(new ApiSubError(
                ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
                cv.getMessage()));
    }

    public void addApiSubError(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.forEach(this::addApiSubError);
    }
}