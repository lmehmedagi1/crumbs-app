package com.crumbs.recipeservice.controllers;

import com.crumbs.recipeservice.RecipeServiceApplication;
import com.crumbs.recipeservice.requests.DietRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.reactive.function.BodyInserters;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {RecipeServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureWebTestClient
class DietControllerTest {

    private MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    private WebTestClient webTestClient;

    //bb244361-88cb-14eb-8ecd-0242ac130003
    @Test
    void testGetAllDiets() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/diets";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testGetDietByIdSuccess() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String id = "87940ca7-807d-4d1b-af2e-f6a0c22ed2c8";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/diets")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testGetDietByIdFailNotExist() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String id = "bb244361-88cb-14eb-8ecd-0242ac130007";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/diets")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testCreateCategorySuccess() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/diets";
        String inputJson = "{" +
                "    \"title\": \"Test title\"," +
                "    \"description\": \"Test description\"," +
                "    \"duration\": \"78\"," +
                "    \"is_private\": \"false\"," +
                "    \"user_id\": \"75a8f34b-2539-452a-9325-b432dbe3b995\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testCreateDietNullTitle() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/category";
        String inputJson = "{" +
                "    \"description\": \"Test description\"," +
                "    \"duration\": \"78\"," +
                "    \"is_private\": \"false\"," +
                "    \"user_id\": \"75a8f34b-2539-452a-9325-b432dbe3b995\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testCreateDietNullDescription() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/category";
        String inputJson = "{" +
                "    \"title\": \"Test title\"," +
                "    \"duration\": \"78\"," +
                "    \"is_private\": \"false\"," +
                "    \"user_id\": \"75a8f34b-2539-452a-9325-b432dbe3b995\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testUpdateDietInvalidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/diets";
        String inputJson = "{" +
                "    \"title\": \"Test title\"," +
                "    \"description\": \"Test description\"," +
                "    \"duration\": \"78\"," +
                "    \"is_private\": \"false\"," +
                "    \"user_id\": \"75a8f34b-2539-452a-9325-b432dbe3b995\"" +
                "}";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .param("id", "75a8f34b-2539-452a-9325-b432dbe3b995")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testUpdateDietValidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/diets";
        String inputJson = "{" +
                "    \"title\": \"Test title11\"," +
                "    \"description\": \"Test description\"," +
                "    \"duration\": \"78\"," +
                "    \"is_private\": \"false\"," +
                "    \"user_id\": \"75a8f34b-2539-452a-9325-b432dbe3b995\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .param("id", "add537e6-12a6-410a-83f3-7c9cc95c2d7a")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testUpdateDietNullTitle() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/diets";
        String inputJson = "{" +
                "    \"description\": \"Test description\"," +
                "    \"duration\": \"78\"," +
                "    \"is_private\": \"false\"," +
                "    \"user_id\": \"75a8f34b-2539-452a-9325-b432dbe3b995\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .param("id", "bb244361-88cb-14eb-8ecd-0242ac130003")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteDietInvalidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "bb244361-88cb-14eb-8ecd-0242ac130007";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/diets")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteDietValidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "87940ca7-807d-4d1b-af2e-f6a0c22ed2c8";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/diets")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.NO_CONTENT.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testCreateDietValidUser() throws Exception {
        String medo_id = "d913320a-baf1-43e0-b8b7-25f748e574ee";
        DietRequest dietRequest = new DietRequest(medo_id, "LowDo",
                "Arrived totally in as between private. Favour of so as on pretty though elinor direct. Reasonable estimating be alteration we themselves entreaties me of reasonably",
                7, true);
        String sport_diet = "87940ca7-807d-4d1b-af2e-f6a0c22ed2c8";

        webTestClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path("/diets")
                        .queryParam("id", sport_diet)
                        .build()).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(dietRequest))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

    @Test
    void testCreateDietInvalidUser() throws Exception {
        String medo_id = "d913320a-baf1-43e0-b8b7-25f748e574bb";
        DietRequest dietRequest = new DietRequest(medo_id, "LowDo",
                "Arrived totally in as between private. Favour of so as on pretty though elinor direct. Reasonable estimating be alteration we themselves entreaties me of reasonably",
                7, true);
        String sport_diet = "87940ca7-807d-4d1b-af2e-f6a0c22ed2c8";

        webTestClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path("/diets")
                        .queryParam("id", sport_diet)
                        .build()).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(dietRequest))
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void testUpdateDietInvalidUser() throws Exception {
        String medo_id = "d913320a-baf1-43e0-b8b7-25f748e574bb";
        DietRequest dietRequest = new DietRequest(medo_id, "LowDo",
                "Arrived totally in as between private. Favour of so as on pretty though elinor direct. Reasonable estimating be alteration we themselves entreaties me of reasonably",
                7, true);
        String sport_diet = "87940ca7-807d-4d1b-af2e-f6a0c22ed2c8";

        webTestClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path("/diets")
                        .queryParam("id", sport_diet)
                        .build()).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(dietRequest))
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void testUpdateDietValidUser() throws Exception {
        String medo_id = "d913320a-baf1-43e0-b8b7-25f748e574ee";
        DietRequest dietRequest =
                new DietRequest(medo_id, "LowDo",
                        "Arrived totally in as between private. Favour of so as on pretty though elinor direct. " +
                                "Reasonable estimating be alteration we themselves entreaties me of reasonably",
                        7, true);
        String sport_diet = "87940ca7-807d-4d1b-af2e-f6a0c22ed2c8";

        webTestClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path("/diets")
                        .queryParam("id", sport_diet)
                        .build()).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(dietRequest))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }
}
