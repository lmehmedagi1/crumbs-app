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

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {RecipeServiceApplication.class})
@Transactional
class RecipeControllerTest {
    private MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;

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

        String id = "fb244360-88cb-11eb-8dcd-0242ac130003";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/recipe")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testGetRecipeInvalidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "fb244360-88cb-11eb-8dcd-0242ac130009";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/recipe")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testCreateRecipeSuccess() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/recipes/create";
        String inputJson = "{" +
                "    \"description\": \"Test recipe\"," +
                "    \"method\": \"Test recipe method\"," +
                "    \"title\": \"Best title ever\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testCreateRecipeNullTitle() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/recipes/create";
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
        String uri = "/recipes/create";
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
        String uri = "/recipes/update";
        String inputJson = "{" +
                "    \"id\": \"fb244360-88cb-11eb-8dcd-0242ac130009\"," +
                "    \"description\": \"Test Update Description\"," +
                "    \"method\": \"Test recipe method update\"," +
                "    \"title\": \"Best title ever\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testUpdateRecipeValidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/recipes/update";
        String inputJson = "{" +
                "    \"id\": \"fb244360-88cb-11eb-8dcd-0242ac130003\"," +
                "    \"description\": \"Test Update Description\"," +
                "    \"method\": \"Test recipe method update\"," +
                "    \"title\": \"Best title ever\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testUpdateRecipeNullMethod() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/recipes/update";
        String inputJson = "{" +
                "    \"id\": \"fb244360-88cb-11eb-8dcd-0242ac130003\"," +
                "    \"description\": \"Test Update Description\"," +
                "    \"title\": \"Best title ever\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteRecipeInvalidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "fb244360-88cb-11eb-8dcd-0242ac130009";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/recipes/delete")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteRecipeValidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "fb244360-88cb-11eb-8dcd-0242ac130003";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/recipes/delete")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }
}