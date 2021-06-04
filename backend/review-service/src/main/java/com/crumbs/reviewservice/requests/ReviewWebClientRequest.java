package com.crumbs.reviewservice.requests;

import com.crumbs.reviewservice.exceptions.RecipeNotFoundException;
import com.crumbs.reviewservice.exceptions.UserNotFoundException;
import com.crumbs.reviewservice.models.Recipe;
import com.crumbs.reviewservice.models.User;
import com.crumbs.reviewservice.projections.UserClassView;
import com.crumbs.reviewservice.projections.UserRecipeView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
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

    public Recipe checkIfRecipeExists(UUID recipeId) {
        return webClientBuilder.baseUrl("http://recipe-service").build().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/recipes/recipe")
                        .queryParam("id", recipeId)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new RecipeNotFoundException()))
                .bodyToMono(Recipe.class)
                .block();
    }

    public UserRecipeView getRecipeViewById(UUID recipeId) {
        return webClientBuilder.baseUrl("http://recipe-service").build().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/recipes/view")
                        .queryParam("id", recipeId)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new RecipeNotFoundException()))
                .bodyToMono(UserRecipeView.class)
                .block();
    }

    public UserRecipeView getDietViewById(UUID dietId) {
        return webClientBuilder.baseUrl("http://recipe-service").build().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/diet/view")
                        .queryParam("id", dietId)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new RecipeNotFoundException()))
                .bodyToMono(UserRecipeView.class)
                .block();
    }

    public UserClassView getUserPreview(UUID id) {
        return webClientBuilder.baseUrl("http://user-service").build().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/account/view")
                        .queryParam("id", id)
                        .build())
                .accept(MediaTypes.HAL_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new UserNotFoundException()))
                .bodyToMono(UserClassView.class)
                .block();
    }


}
