package com.crumbs.reviewservice.controllers;

import com.crumbs.reviewservice.ReviewServiceApplication;
import com.crumbs.reviewservice.utility.JwtConfigAndUtil;
import io.jsonwebtoken.Jwts;
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

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    public String generateToken(@NotNull String id) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, id);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(10)))
                .signWith(new JwtConfigAndUtil().getSecretKey()).compact();
    }

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
                                .path("/reviews/comments")
                                .queryParam("recipeId", "2e0233d2-6e01-455c-8724-2117ad252ced")
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
                "  \"is_liked\": false,\n" +
                "  \"rating\": 5,\n" +
                "  \"entity_id\": \"d3cd7d6f-b9c5-40aa-bbd0-487c47411b8a\",\n" +
                "  \"entity_type\": \"recipe\"\n" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization","Bearer " +  generateToken("3e8ec94c-3edf-49e0-b548-425088881f60"))
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
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
    void testGetMonthlySuccess() throws Exception {
        String uri = "/reviews/top-monthly";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("pageNo", "0")).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testGetDailySuccess() throws Exception {
        String uri = "/reviews/top-daily";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("pageNo", "0")).andReturn();
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
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
        String id = "9469a486-1e50-4aaf-a760-1daf770a2147";
        String uri = "/reviews";
        String inputJson = "{" +
                "    \"entity_id\": \"d3cd7d6f-b9c5-40aa-bbd0-487c47411b8a\"," +
                "    \"entity_type\": \"recipe\"," +
                "    \"is_liked\": true," +
                "    \"rating\": 4," +
                "    \"comment\": \"neki komentar1\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .param("id", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson).header("Authorization","Bearer " +  generateToken("3e8ec94c-3edf-49e0-b548-425088881f60"))).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }
 @Test
    void testUpdateReviewInvalidRecipeId() throws Exception {
        String id = "9469a486-1e50-4aaf-a760-1daf770a2147";
        String uri = "/reviews";
        String inputJson = "{" +
                "    \"entity_id\": \"ac8ff8ff-7193-4c45-90bd-9c772cc0494a\"," +
                "    \"is_liked\": true," +
                "    \"rating\": 4," +
                "    \"comment\": \"neki komentar1\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .param("id", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson).header("Authorization","Bearer " +  generateToken("3e8ec94c-3edf-49e0-b548-425088881f60"))).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
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
        String id = "df41bb67-1ae7-49e4-b4b3-c170845a2241";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/reviews")
                .param("id", id).header("Authorization","Bearer " +  generateToken("3e8ec94c-3edf-49e0-b548-425088881f60"))).andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteReviewValidId() throws Exception {
        String id = "df41bb67-1ae7-49e4-b4b3-c170845a2249";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/reviews")
                .param("id", id).header("Authorization","Bearer " + generateToken("3e8ec94c-3edf-49e0-b548-425088881f60"))).andReturn();

        assertEquals(204, mvcResult.getResponse().getStatus());
    }
}
