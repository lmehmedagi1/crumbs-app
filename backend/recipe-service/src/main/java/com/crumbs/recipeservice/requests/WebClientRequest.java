package com.crumbs.recipeservice.requests;

import com.crumbs.recipeservice.exceptions.RecipeNotFoundException;
import com.crumbs.recipeservice.exceptions.UserNotFoundException;
import com.crumbs.recipeservice.models.User;
import com.crumbs.recipeservice.responses.ListWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
public class WebClientRequest {
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public WebClientRequest(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public User getRecipeAuthor(UUID authorId) {
        return webClientBuilder.baseUrl("http://user-service").build().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/account")
                        .queryParam("id", authorId)
                        .build())
                .accept(MediaTypes.HAL_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new UserNotFoundException()))
                .bodyToMono(User.class)
                .block();
    }

    public UUID[] getTopMonthlyRecepies(int pageNo, String jwt) {
        return webClientBuilder.baseUrl("http://review-service").build().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/reviews/topMonthly")
                        .queryParam("pageNo", pageNo)
                        .build())
                .header("Authorization", jwt)
                .accept(MediaTypes.HAL_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new RecipeNotFoundException()))
                .bodyToMono(UUID[].class)
                .block();
    }

    public Double getRecipeRating(UUID recipeId) {
        return webClientBuilder.baseUrl("http://review-service").build().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/reviews/rating")
                        .queryParam("recipeId", recipeId)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new RecipeNotFoundException()))
                .bodyToMono(Double.class)
                .block();
    }

    public User checkIfUserExists(String jwt) {
        return webClientBuilder.baseUrl("http://user-service").build().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/account")
                        .build())
                .header("Authorization", jwt)
                .accept(MediaTypes.HAL_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new UserNotFoundException()))
                .bodyToMono(User.class)
                .block();
    }

    public UUID[] getTopDailyRecepies(Integer pageNo, String jwt) {
        return webClientBuilder.baseUrl("http://review-service").build().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/reviews/topDaily")
                        .queryParam("pageNo", pageNo)
                        .build())
                .header("Authorization", jwt)
                .accept(MediaTypes.HAL_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new RecipeNotFoundException()))
                .bodyToMono(UUID[].class)
                .block();
    }
}
