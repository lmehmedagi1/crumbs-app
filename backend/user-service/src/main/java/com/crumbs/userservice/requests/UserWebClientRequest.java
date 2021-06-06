package com.crumbs.userservice.requests;

import com.crumbs.userservice.exceptions.InvalidJsonRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserWebClientRequest {
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public UserWebClientRequest(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public String sendUserSubscribedNotification(NotificationRequest notification) {
        return webClientBuilder.baseUrl("http://notification-service").build().post()
                .uri("/notifications")
                .body(Mono.just(notification), NotificationRequest.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new InvalidJsonRequest()))
                .bodyToMono(String.class)
                .block();
    }

}
