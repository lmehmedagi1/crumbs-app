package com.crumbs.systemevents.exceptions;

import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;

@GrpcAdvice
public class GrpcExceptionAdvice {

    @GrpcExceptionHandler(TransactionSystemException.class)
    public Status handleTransactionSystemException(TransactionSystemException e) {
        return Status.ABORTED.withDescription(e.getLocalizedMessage()).withCause(e);
    }

    @GrpcExceptionHandler(MethodArgumentNotValidException.class)
    public Status handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Status.INVALID_ARGUMENT.withDescription("Check your request and try again!").withCause(e);
    }

    @GrpcExceptionHandler(javax.validation.ConstraintViolationException.class)
    public Status handleConstraintViolation(javax.validation.ConstraintViolationException e) {
        return Status.INVALID_ARGUMENT.withDescription("Check your request and try again!").withCause(e);
    }

    /**
     * Handle generic Exception.class
     */
    @GrpcExceptionHandler(Exception.class)
    public Status handleException(Exception e) {
        return Status.INTERNAL.withDescription("Check your request and try again!");
    }
}