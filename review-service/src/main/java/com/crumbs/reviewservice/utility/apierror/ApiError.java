package com.crumbs.reviewservice.utility.apierror;

import com.crumbs.reviewservice.utility.LowerCaseClassNameResolver;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import lombok.Data;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM, property = "error", visible = true)
@JsonTypeIdResolver(LowerCaseClassNameResolver.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public
class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String status;
    private Integer code;
    private String error;
    private List<ApiSubError> message = new ArrayList<>();

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus httpStatus) {
        this();
        this.status = httpStatus.getReasonPhrase();
        this.code = httpStatus.value();
    }

    public ApiError(HttpStatus status, Throwable ex) {
        this(status);
        this.error = "Unexpected error";
    }

    public ApiError(HttpStatus status, String error) {
        this(status);
        this.error = error;
    }

    private void addSubError(ApiSubError subError) {
        if (message == null)
            message = new ArrayList<>();
        message.add(subError);
    }

    private void addValidationError(String field, Object rejectedValue, String message) {
        addSubError(new ApiValidationError(field, rejectedValue, message));
    }

    private void addValidationError(String message) {
        addSubError(new ApiValidationError(message));
    }

    private void addValidationError(FieldError fieldError) {
        this.addValidationError(
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage());
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError);
    }

    private void addValidationError(ObjectError objectError) {
        this.addValidationError(objectError.getDefaultMessage());
    }

    public void addValidationError(List<ObjectError> globalErrors) {
        globalErrors.forEach(this::addValidationError);
    }

    /**
     * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
     *
     * @param cv the ConstraintViolation
     */
    private void addValidationError(ConstraintViolation<?> cv) {
        this.addValidationError(
                ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
                cv.getInvalidValue(),
                cv.getMessage());
    }

    public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.forEach(this::addValidationError);
    }
}