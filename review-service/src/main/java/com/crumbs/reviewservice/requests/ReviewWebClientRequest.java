package com.crumbs.reviewservice.requests;

import com.crumbs.reviewservice.exceptions.RecipeNotFoundException;
import com.crumbs.reviewservice.exceptions.UserNotFoundException;
import com.crumbs.reviewservice.models.Recipe;
import com.crumbs.reviewservice.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class ReviewWebClientRequest {

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public ReviewWebClientRequest(WebClient.Builder webClientBuilder) {
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

    public Recipe checkIfRecipeExists(UUID recipeId) {
        return webClientBuilder.baseUrl("http://recipe-service").build().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/recipes")
                        .queryParam("id", recipeId)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new RecipeNotFoundException()))
                .bodyToMono(Recipe.class)
                .block();
    }
}
