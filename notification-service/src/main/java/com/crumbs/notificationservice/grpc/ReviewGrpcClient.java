package com.crumbs.notificationservice.grpc;

import com.crumbs.systemevents.grpc.ActionRequest;
import com.crumbs.systemevents.grpc.ActionResponse;
import com.crumbs.systemevents.grpc.LogServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class ReviewGrpcClient {

    public String log(String microservice, String actionType, String resourceName, String responseType) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        LogServiceGrpc.LogServiceBlockingStub stub = LogServiceGrpc.newBlockingStub(channel);
        ActionResponse helloResponse = stub.log(ActionRequest.newBuilder()
                .setTimestamp(LocalDateTime.now(ZoneId.of("UTC")).toString())
                .setServiceName(microservice)
                .setActionType(actionType)
                .setResourceName(resourceName)
                .setResponseType(responseType)
                .build());
        channel.shutdown();
        return helloResponse.getResponseMessage();
    }
}