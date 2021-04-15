package com.crumbs.notificationservice.utility;

import com.crumbs.notificationservice.grpc.ReviewGrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.http.HttpStatus;

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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println(reviewGrpcClient.log("notification-service", request.getMethod(), "Notification", HttpStatus.valueOf(response.getStatus()).toString()));
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}