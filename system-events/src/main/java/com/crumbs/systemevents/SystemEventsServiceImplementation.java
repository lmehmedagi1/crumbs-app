package com.crumbs.systemevents;

import com.crumbs.systemevents.grpc.ActionRequest;
import com.crumbs.systemevents.grpc.ActionResponse;
import com.crumbs.systemevents.grpc.LogActionGrpc;
import io.grpc.stub.StreamObserver;
import model.SystemEventsModel;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import repositories.SystemEventsRepository;

@GRpcService
public class SystemEventsServiceImplementation extends LogActionGrpc.LogActionImplBase {

    @Autowired
    SystemEventsRepository systemEventsRepository;

    @Override
    public void log(ActionRequest request, StreamObserver<ActionResponse> responseObserver) {
        SystemEventsModel event = new SystemEventsModel(
                0,
                request.getTimestamp(),
                request.getServiceName(),
                request.getActionType(),
                request.getResourceName(),
                request.getResponseType()
                );

        systemEventsRepository.save(event);
        ActionResponse response = ActionResponse.newBuilder()
                .setResponseMessage("OK")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}