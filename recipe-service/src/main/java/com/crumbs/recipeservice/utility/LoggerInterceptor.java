package com.crumbs.recipeservice.utility;

import com.crumbs.recipeservice.grpc.RecipeGrpcClient;
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
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String resource = request.getRequestURI();
        String resourceName = resource.substring(1, 2).toUpperCase() + resource.substring(2);
        System.out.println(resourceName);
        System.out.println(recipeGrpcClient.log("Recipe service", request.getMethod(), resourceName, HttpStatus.valueOf(response.getStatus()).toString()));
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
