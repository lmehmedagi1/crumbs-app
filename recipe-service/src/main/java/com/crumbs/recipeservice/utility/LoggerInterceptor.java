package com.crumbs.recipeservice.utility;

import com.crumbs.recipeservice.grpc.RecipeGrpcClient;
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

    private final RecipeGrpcClient recipeGrpcClient;

    @Autowired
    public LoggerInterceptor(RecipeGrpcClient recipeGrpcClient) {
        this.recipeGrpcClient = recipeGrpcClient;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String resource = request.getRequestURI();
        String resourceName = resource.substring(1, 2).toUpperCase() + resource.substring(2);
        Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);
        String grpcResponse = recipeGrpcClient.log("Recipe service", resourceName, request.getMethod(),
                HttpStatus.valueOf(response.getStatus()).toString());
        logger.info(grpcResponse);
    }
}
