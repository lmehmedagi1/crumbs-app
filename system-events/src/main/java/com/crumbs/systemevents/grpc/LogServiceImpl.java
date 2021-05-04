package com.crumbs.systemevents.grpc;

import com.crumbs.systemevents.models.SystemEvent;
import com.crumbs.systemevents.repositories.SystemEventsRepository;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneId;

@GRpcService
public class LogServiceImpl extends LogServiceGrpc.LogServiceImplBase {

    private final SystemEventsRepository systemEventsRepository;

    @Autowired
    public LogServiceImpl(SystemEventsRepository systemEventsRepository) {
        this.systemEventsRepository = systemEventsRepository;
    }

    @Override
    public void log(ActionRequest request, StreamObserver<ActionResponse> responseObserver) {
        SystemEvent systemEvent = new SystemEvent();
        systemEvent.setServiceName(request.getServiceName());
        systemEvent.setActionType(request.getActionType());
        systemEvent.setTimestamp(LocalDateTime.now(ZoneId.of("UTC")));
        systemEvent.setResourceName(request.getResourceName());
        systemEvent.setResponseType(request.getResponseType());
        systemEvent.setMessage(request.getMessage());

        systemEventsRepository.save(systemEvent);

        ActionResponse response = ActionResponse.newBuilder()
                .setResponseMessage("OK")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
