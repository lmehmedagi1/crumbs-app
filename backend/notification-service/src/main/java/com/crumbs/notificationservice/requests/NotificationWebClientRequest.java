package com.crumbs.notificationservice.requests;

import com.crumbs.notificationservice.exceptions.UserNotFoundException;
import com.crumbs.notificationservice.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class NotificationWebClientRequest {
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public NotificationWebClientRequest(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public User checkIfUserExists(UUID userId) {
        return webClientBuilder.baseUrl("http://user-service").build().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/account")
                        .queryParam("id", userId)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new UserNotFoundException()))
                .bodyToMono(User.class)
                .block();
    }

}
