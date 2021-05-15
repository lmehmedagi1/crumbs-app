package com.crumbs.notificationservice.utility;

import com.crumbs.notificationservice.grpc.NotificationGrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoggerInterceptor implements HandlerInterceptor {

    private final NotificationGrpcClient notificationGrpcClient;

    @Autowired
    public LoggerInterceptor(NotificationGrpcClient notificationGrpcClient) {
        this.notificationGrpcClient = notificationGrpcClient;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);
        String grpcResponse = notificationGrpcClient.log("Notification service", "Notification",
                request.getMethod(), HttpStatus.valueOf(response.getStatus()).toString());
        logger.info(grpcResponse);
    }
}