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

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {RecipeServiceApplication.class})
@Transactional
class DietControllerTest {

    private MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;
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
        String id = "bb244361-88cb-14eb-8ecd-0242ac130003";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/diet")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }


    @Test
    void testGetDietByIdFailNotExist() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String id = "bb244361-88cb-14eb-8ecd-0242ac130007";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/diet")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testCreateCategorySuccess() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/diet/create";
        String inputJson = "{" +
                "    \"title\": \"Test title\"," +
                "    \"description\": \"Test description\"," +
                "    \"duration\": \"78\"," +
                "    \"isPrivate\": \"false\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testCreateDietNullTitle() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/category/create";
        String inputJson = "{" +
                "    \"description\": \"Test description\"," +
                "    \"duration\": \"78\"," +
                "    \"isPrivate\": \"false\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testCreateDietNullDescription() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/category/create";
        String inputJson = "{" +
                "    \"title\": \"Test title\"," +
                "    \"duration\": \"78\"," +
                "    \"isPrivate\": \"false\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testUpdateDietInvalidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/diet/update";
        String inputJson = "{" +
                "    \"id\": \"bb244361-88cb-14eb-8ecd-0242ac130007\"," +
                "    \"title\": \"Test title\"," +
                "    \"description\": \"Test description\"," +
                "    \"duration\": \"78\"," +
                "    \"isPrivate\": \"false\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testUpdateDietValidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/diet/update";
        String inputJson = "{" +
                "    \"id\": \"bb244361-88cb-14eb-8ecd-0242ac130003\"," +
                "    \"title\": \"Test title11\"," +
                "    \"description\": \"Test description\"," +
                "    \"duration\": \"78\"," +
                "    \"isPrivate\": \"false\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testUpdateDietNullTitle() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/diet/update";
        String inputJson = "{" +
                "    \"id\": \"bb244361-88cb-14eb-8ecd-0242ac130003\"," +
                "    \"description\": \"Test description\"," +
                "    \"duration\": \"78\"," +
                "    \"isPrivate\": \"false\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteDietInvalidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "bb244361-88cb-14eb-8ecd-0242ac130007";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/diet/delete")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteDietValidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "bb244361-88cb-14eb-8ecd-0242ac130003";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/diet/delete")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }
}
