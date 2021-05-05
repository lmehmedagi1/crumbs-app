package com.crumbs.reviewservice.utility;

import com.crumbs.reviewservice.grpc.ReviewGrpcClient;
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

    private final ReviewGrpcClient reviewGrpcClient;

    @Autowired
    public LoggerInterceptor(ReviewGrpcClient reviewGrpcClient) {
        this.reviewGrpcClient = reviewGrpcClient;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);
        String grpcResponse = reviewGrpcClient.log("Review service", "Review",
                request.getMethod(), HttpStatus.valueOf(response.getStatus()).toString());

        logger.info(grpcResponse);
    }
}
