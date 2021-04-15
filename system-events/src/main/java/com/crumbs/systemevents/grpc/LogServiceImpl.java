package com.crumbs.systemevents.grpc;

import com.crumbs.systemevents.repositories.SystemEventsRepository;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

import com.crumbs.systemevents.models.SystemEvent;
import com.crumbs.systemevents.repositories.SystemEventsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@GRpcService
public class LogServiceImpl extends LogServiceGrpc.LogServiceImplBase {

    @Autowired
    private SystemEventsRepository systemEventsRepository;

    @Override
    public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        responseObserver.onNext(HelloResponse.newBuilder().setGreeting("Sup " + request.getFirstName()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void log(ActionRequest request, StreamObserver<ActionResponse> responseObserver) {
        SystemEvent systemEvent = new SystemEvent();
        systemEvent.setServiceName(request.getServiceName());
        systemEvent.setActionType(request.getActionType());
        systemEvent.setTimestamp(LocalDateTime.now(ZoneId.of("UTC")));
        systemEvent.setResourceName(request.getResourceName());
        systemEvent.setResponseType(request.getResponseType());

        systemEventsRepository.save(systemEvent);

        ActionResponse response = ActionResponse.newBuilder()
                .setResponseMessage("OK")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
