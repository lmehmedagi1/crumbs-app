package com.crumbs.systemevents.grpc;

import com.crumbs.systemevents.models.SystemEvent;
import com.crumbs.systemevents.repositories.SystemEventsRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@GrpcService
@Validated
public class LogServiceImpl extends LogServiceGrpc.LogServiceImplBase {

    private final SystemEventsRepository systemEventsRepository;

    @Autowired
    public LogServiceImpl(SystemEventsRepository systemEventsRepository) {
        this.systemEventsRepository = systemEventsRepository;
    }

    private void validateAndLog(ActionRequest actionRequest) {
        SystemEvent systemEvent = new SystemEvent();
        systemEvent.setServiceName(actionRequest.getServiceName());
        systemEvent.setResourceName(actionRequest.getResourceName());
        systemEvent.setMethod(actionRequest.getMethod());
        systemEvent.setResponseStatus(actionRequest.getResponseStatus());

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        validator.validate(systemEvent);
        Set<ConstraintViolation<SystemEvent>> violations = validator.validate(systemEvent);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);

        try {
            systemEventsRepository.save(systemEvent);
        } catch (Exception e) {
            throw new TransactionSystemException("Log unsuccessful!");
        }
    }

    @Override
    public void log(ActionRequest request, StreamObserver<ActionResponse> responseObserver) {
        ActionResponse response = ActionResponse.newBuilder()
                .setResponseMessage("Log successful!")
                .build();

        validateAndLog(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
