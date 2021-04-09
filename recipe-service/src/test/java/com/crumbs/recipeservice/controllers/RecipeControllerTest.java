package com.crumbs.recipeservice.controllers;

import com.crumbs.recipeservice.RecipeServiceApplication;
import com.crumbs.recipeservice.requests.RecipeRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {RecipeServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureWebTestClient
class RecipeControllerTest {
    private MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testGetAllRecipesSuccess() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/recipes";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testGetRecipeValidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "d3cd7d6f-b9c5-40aa-bbd0-487c47411b8a";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/recipes")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testGetRecipeInvalidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "fb244360-88cb-11eb-8dcd-0242ac130009";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/recipes")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testCreateRecipeSuccess() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/recipes";
        String inputJson = "{" +
                "    \"description\": \"Test recipe\"," +
                "    \"method\": \"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.\"," +
                "    \"title\": \"Best title ever\"," +
                "    \"user_id\": \"fc98612b-c0c2-4792-b2ac-acb7dd6555c1\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testCreateRecipeNullTitle() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/recipes";
        String inputJson = "{" +
                "    \"description\": \"Test recipe\"," +
                "    \"method\": \"Test recipe method\"," +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testCreateRecipeNullMethod() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/recipes";
        String inputJson = "{" +
                "    \"description\": \"Test recipe\"," +
                "    \"title\": \"Best title ever\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testUpdateRecipeInvalidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/recipes";
        String inputJson = "{" +
                "    \"description\": \"Test Update Description\"," +
                "    \"method\": \"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.\"," +
                "    \"title\": \"Best title ever\"," +
                "    \"user_id\": \"fc98612b-c0c2-4792-b2ac-acb7dd6555c1\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .param("id", "fb244360-88cb-11eb-8dcd-0242ac130009")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testUpdateRecipeValidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/recipes";
        String inputJson = "{" +
                "    \"description\": \"Test Update Description\"," +
                "    \"method\": \"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.\"," +
                "    \"title\": \"Best title ever\"," +
                "    \"user_id\": \"fc98612b-c0c2-4792-b2ac-acb7dd6555c1\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .param("id", "2e0233d2-6e01-455c-8724-2117ad252ced")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testUpdateRecipeNullMethod() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/recipes";
        String inputJson = "{" +
                "    \"description\": \"Test Update Description\"," +
                "    \"title\": \"Best title ever\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .param("id", "fb244360-88cb-11eb-8dcd-0242ac130003")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteRecipeInvalidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "fb244360-88cb-11eb-8dcd-0242ac130009";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/recipes")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteRecipeValidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "2e0233d2-6e01-455c-8724-2117ad252ced";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/recipes")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.NO_CONTENT.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testCreateRecipeValidUser() throws Exception {
        String medo_id = "d913320a-baf1-43e0-b8b7-25f748e574ee";
        RecipeRequest recipeRequest = new RecipeRequest(medo_id, "LowDo",
                "Arrived totally in as between private. Favour of so as on pretty though elinor direct. Reasonable estimating be alteration we themselves entreaties me of reasonably",
                "Favour of so as on pretty though elinor direct. Favour of so as on pretty though elinor direct. ");

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/recipes")
                        .queryParam("userId", "d913320a-baf1-43e0-b8b7-25f748e574ee", "token")
                        .build()).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(recipeRequest))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

    @Test
    void testCreateRecipeInvalidUser() throws Exception {
        String medo_id = "d913320a-baf1-43e0-b8b7-25f748e574bb";
        RecipeRequest recipeRequest = new RecipeRequest(medo_id, "LowDo",
                "Arrived totally in as between private. Favour of so as on pretty though elinor direct. Reasonable estimating be alteration we themselves entreaties me of reasonably",
                "Favour of so as on pretty though elinor direct. Favour of so as on pretty though elinor direct. ");

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/recipes")
                        .queryParam("userId", "d913320a-baf1-43e0-b8b7-25f748e574ee", "token")
                        .build()).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(recipeRequest))
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void testUpdateRecipeInvalidUser() throws Exception {
        String medo_id = "d913320a-baf1-43e0-b8b7-25f748e574bb";
        RecipeRequest recipeRequest = new RecipeRequest(medo_id, "LowDo",
                "Arrived totally in as between private. Favour of so as on pretty though elinor direct. Reasonable estimating be alteration we themselves entreaties me of reasonably",
                "Favour of so as on pretty though elinor direct. Favour of so as on pretty though elinor direct. ");
        String sampita_id = "d3cd7d6f-b9c5-40aa-bbd0-487c47411b8a";

        webTestClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path("/recipes")
                        .queryParam("id", sampita_id)
                        .build()).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(recipeRequest))
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void testUpdateRecipeValidUser() throws Exception {
        String medo_id = "d913320a-baf1-43e0-b8b7-25f748e574ee";
        RecipeRequest recipeRequest = new RecipeRequest(medo_id, "LowDo",
                "Arrived totally in as between private. Favour of so as on pretty though elinor direct. Reasonable estimating be alteration we themselves entreaties me of reasonably",
                "Favour of so as on pretty though elinor direct. Favour of so as on pretty though elinor direct. ");
        String sampita_id = "d3cd7d6f-b9c5-40aa-bbd0-487c47411b8a";

        webTestClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path("/recipes")
                        .queryParam("id", sampita_id)
                        .build()).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(recipeRequest))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }
}