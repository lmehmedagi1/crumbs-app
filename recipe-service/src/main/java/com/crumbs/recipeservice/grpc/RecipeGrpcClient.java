package com.crumbs.recipeservice.grpc;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class RecipeGrpcClient {

    @GrpcClient("recipe-service-grpc")
    private LogServiceGrpc.LogServiceBlockingStub logServiceBlockingStub;

    public String log(String serviceName, String resourceName, String method, String responseStatus) {
        try {
            ActionResponse helloResponse = logServiceBlockingStub.log(ActionRequest.newBuilder()
                    .setServiceName(serviceName)
                    .setResourceName(resourceName)
                    .setMethod(method)
                    .setResponseStatus(responseStatus)
                    .build());

            return helloResponse.getResponseMessage();
        } catch (Exception ignored) {
            return "Exception during gRPC request";
        }
    }
}
