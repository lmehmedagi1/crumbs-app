package com.crumbs.reviewservice.controllers;

import com.crumbs.reviewservice.ReviewServiceApplication;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

//@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {ReviewServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureWebTestClient
//@WebFluxTest(MyControllerTest.class)
class ReviewControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    private WebTestClient webTestClient;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetAllReviews() throws Exception {
        String uri = "/reviews";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testGetReviewByIdSuccess() throws Exception {
        String id = "9469a486-1e50-4aaf-a760-1daf770a2147";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/reviews")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }


    @Test
    void testGetReviewByIdFailNotExist() throws Exception {
        String id = "5ccafc30-b1b3-4f74-ba3c-79583a3129a1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/reviews")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testGetReviewsByRecipeIdSuccess() throws Exception {
        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/reviews")
                                .queryParam("recipeId", "ac8ff8ff-7193-4c45-90bd-9c662cc0494a")
                                .build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

    @Test
    void testCreateReviewSuccess() throws Exception {
        String uri = "/reviews";
        String inputJson = "{\n" +
                "  \"comment\": \"Novi komentar\",\n" +
                "  \"is_liked\": true,\n" +
                "  \"rating\": 5,\n" +
                "  \"recipe_id\": \"d3cd7d6f-b9c5-40aa-bbd0-487c47411b8a\",\n" +
                "  \"user_id\": \"3e8ec94c-3edf-49e0-b548-425088881f60\"\n" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testCreateReviewNullUserId() throws Exception {
        String uri = "/reviews";
        String inputJson = "{" +
                "    \"recipe_id\": \"599971d7-99bb-4d5e-b65f-598327727dd7\"," +
                "    \"is_liked\": \"true\"," +
                "    \"rating\": \"4\"," +
                "    \"comment\": \"neki komentar\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testCreateReviewNullIsLiked() throws Exception {
        String uri = "/reviews";
        String inputJson = "{" +
                "    \"user_id\": \"bb244361-88cb-14eb-8ecd-0242ac130007\"," + // Invalid UUIDs anyway
                "    \"recipe_id\": \"bb244361-88cb-14eb-8ecd-0242ac130008\"," +
                "    \"rating\": \"4\"," +
                "    \"comment\": \"neki komentar\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testCreateReviewTooBigRating() throws Exception {
        String uri = "/reviews";
        String inputJson = "{" +
                "    \"user_id\": \"bb244361-88cb-14eb-8ecd-0242ac130007\"," +
                "    \"recipe_id\": \"bb244361-88cb-14eb-8ecd-0242ac130008\"," +
                "    \"is_liked\": \"true\"," +
                "    \"rating\": \"6\"," +
                "    \"comment\": \"neki komentar\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testUpdateReviewInvalidId() throws Exception {
        String id = "5ccafc30-b1b3-4f74-ba3c-79583assss3129c5";
        String uri = "/reviews";
        String inputJson = "{" +
                "    \"user_id\": \"5ccafc30-b1b3-4f74-ba3c-79583a3129c6\"," +
                "    \"recipe_id\": \"5ccafc30-b1b3-4f74-ba3c-79583a3129c7\"," +
                "    \"is_liked\": true," +
                "    \"rating\": 4," +
                "    \"comment\": \"neki komentar\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .param("id", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testUpdateReviewValidId() throws Exception {
        String id = "53f5e7d3-1942-4ff4-85cf-401a4b7b1334";
        String uri = "/reviews";
        String inputJson = "{" +
                "    \"user_id\": \"3e8ec94c-3edf-49e0-b548-425088881f60\"," +
                "    \"recipe_id\": \"ac8ff8ff-7193-4c45-90bd-9c662cc0494a\"," +
                "    \"is_liked\": true," +
                "    \"rating\": 4," +
                "    \"comment\": \"neki komentar\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .param("id", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testUpdateReviewNullIsLiked() throws Exception {
        String id = "5ccafc30-b1b3-4f74-ba3c-79583a3129c5";
        String uri = "/reviews";
        String inputJson = "{" +
                "    \"user_id\": \"bb244361-88cb-14eb-8ecd-0242ac130007\"," +
                "    \"recipe_id\": \"bb244361-88cb-14eb-8ecd-0242ac130008\"," +
                "    \"is_liked\": \"true\"," +
                "    \"rating\": \"4\"," +
                "    \"comment\": \"neki komentar\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .param("id", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteReviewInvalidId() throws Exception {
        String id = "5ccafc30-b1b3-4f74-ba3c-79583a3129f9";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/reviews")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteReviewValidId() throws Exception {
        String id = "22b01334-417b-4a8f-8fb6-79bf003fdf4d";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/reviews")
                .param("id", id)).andReturn();

        assertEquals(204, mvcResult.getResponse().getStatus());
    }
}
