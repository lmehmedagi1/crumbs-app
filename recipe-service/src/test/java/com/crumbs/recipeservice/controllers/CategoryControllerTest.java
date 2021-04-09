package com.crumbs.recipeservice.controllers;

import com.crumbs.recipeservice.RecipeServiceApplication;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {RecipeServiceApplication.class})
@Transactional
class CategoryControllerTest {

    private MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Test
    void testGetAllCategories() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/categories";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testGetCategoryByIdSuccess() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String id = "d3f25855-9723-43c2-b29c-34626dd834d2";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/categories")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }


    @Test
    void testGetCategoryByIdFailNotExist() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String id = "fb244361-88cb-14eb-8ecd-0242ac130043";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/categories")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testCreateCategorySuccess() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/categories";
        String inputJson = "{" +
                "\"name\": \"ImeKategorije\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testCreateCategoryNullName() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/categories";
        String inputJson = "{}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testUpdateCategoryInvalidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/categories";

        String inputJson = "{" +
                "    \"name\": \"CategoryName\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .param("id", "fb244361-88cb-14eb-8ecd-0242ac130007")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testUpdateCategoryValidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/categories";
        String inputJson = "{" +
                "    \"name\": \"CategoryName\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .param("id", "943c8274-d329-4d78-92b9-e2f3407a91a4")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testUpdateCategoryNullName() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/categories";
        String inputJson = "{" +
                "    \"id\": \"fb244361-88cb-14eb-8ecd-0242ac130003\"," +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteCategoryInvalidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "fb244361-88cb-14eb-8ecd-0242ac130008";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/categories")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteCategoryValidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "249cf0f8-c344-4b56-8eda-01ceb4ebfceb";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/categories")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.NO_CONTENT.value(), mvcResult.getResponse().getStatus());
    }
}
