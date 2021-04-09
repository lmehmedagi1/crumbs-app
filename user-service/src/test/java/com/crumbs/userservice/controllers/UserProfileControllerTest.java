package com.crumbs.userservice.controllers;

import com.crumbs.userservice.UserServiceApplication;
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
@SpringBootTest(classes = {UserServiceApplication.class})
@Transactional
class UserProfileControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetUserDetailsByIdSuccess() throws Exception {
        String id = "d913320a-baf1-43e0-b8b7-25f748e574ee";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/profile")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testGetUserDetailsIdFailNotExist() throws Exception {
        String id = "d913320a-baf1-43e0-b8b7-25f748e574ef";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/profile")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testUpdateUserDetailsInvalidId() throws Exception {
        String id = "d913320a-baf1-43e0-b8b7-25f748e574ef";
        String uri = "/profile";
        String inputJson = "{" +
                "    \"gender\": \"male\"," +
                "    \"avatar\": \"iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==\"," +
                "    \"firstName\": \"Medolino\"," +
                "    \"lastName\": \"Palda\"," +
                "    \"phoneNumber\": \"666777\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .param("id", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testUpdateUserDetailsValidId() throws Exception {
        String id = "d913320a-baf1-43e0-b8b7-25f748e574ee";
        String uri = "/profile";
        String inputJson = "{" +
                "    \"gender\": \"male\"," +
                "    \"avatar\": \"iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==\"," +
                "    \"firstName\": \"Medolino\"," +
                "    \"lastName\": \"Palda\"," +
                "    \"phoneNumber\": \"666777\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .param("id", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testPartialUpdateUserDetailsTest() throws Exception {
        String id = "d913320a-baf1-43e0-b8b7-25f748e574ee";
        String uri = "/profile";

        String inputJson = "[{  \n" +
                "        \"op\": \"replace\",\n" +
                "        \"path\": \"/gender\",\n" +
                "        \"value\": \"male\"\n" +
                "    }]";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .param("id", id)
                .contentType("application/json-patch+json")
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }
}