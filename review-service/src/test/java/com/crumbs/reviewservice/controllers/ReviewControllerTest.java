package com.crumbs.reviewservice.controllers;

import com.crumbs.reviewservice.ReviewServiceApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {ReviewServiceApplication.class})
@Transactional
class ReviewControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

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
        String id = "5ccafc30-b1b3-4f74-ba3c-79583a3129c5";
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
    void testCreateReviewSuccess() throws Exception {
        String uri = "/reviews";
        String inputJson = "{\n" +
                "  \"comment\": \"Novi komentar\",\n" +
                "  \"is_liked\": true,\n" +
                "  \"rating\": 5,\n" +
                "  \"recipe_id\": \"e3b461d0-96a9-4bcf-836f-35441f57a701\",\n" +
                "  \"user_id\": \"7ab5c6ee-3a70-4d15-a2c4-e11c5ec74095\"\n" +
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
        String id = "5ccafc30-b1b3-4f74-ba3c-79583a3129c5";
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
        String id = "5ccafc30-b1b3-4f74-ba3c-79583a3129c5";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/reviews")
                .param("id", id)).andReturn();

        assertEquals(204, mvcResult.getResponse().getStatus());
    }
}
