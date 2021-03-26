package com.crumbs.reviewservice.controllers;

import com.crumbs.reviewservice.ReviewServiceApplication;
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

    private MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    //bb244361-88cb-14eb-8ecd-0242ac130003
    @Test
    void testGetAllReviews() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/reviews";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testGetReviewByIdSuccess() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String id = "bb244361-88cb-14eb-8ecd-0242ac130003";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/reviews")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }


    @Test
    void testGetReviewByIdFailNotExist() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String id = "bb244361-88cb-14eb-8ecd-0242ac130007";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/reviews")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testCreateReviewSuccess() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/reviews";
        String inputJson = "{" +
                "    \"user_id\": \"bb244361-88cb-14eb-8ecd-0242ac130007\"," +
                "    \"recipe_id\": \"bb244361-88cb-14eb-8ecd-0242ac130008\"," +
                "    \"is_liked\": \"true\"," +
                "    \"rating\": \"4\"," +
                "    \"comment\": \"neki komentar\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testCreateReviewNullUserId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/reviews";
        String inputJson = "{" +
                "    \"recipe_id\": \"bb244361-88cb-14eb-8ecd-0242ac130008\"," +
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
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/reviews";
        String inputJson = "{" +
                "    \"user_id\": \"bb244361-88cb-14eb-8ecd-0242ac130007\"," +
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
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String id = "5ccafc30-b1b3-4f74-ba3c-79583a3129c3";
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

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testUpdateReviewValidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String id = "bb244361-88cb-14eb-8ecd-0242ac130003";
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
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "bb244361-88cb-14eb-8ecd-0242ac130007";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/reviews")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteReviewValidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "bb244361-88cb-14eb-8ecd-0242ac130003";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/reviews")
                .param("id", id)).andReturn();

        assertEquals(204, mvcResult.getResponse().getStatus());
    }
}
