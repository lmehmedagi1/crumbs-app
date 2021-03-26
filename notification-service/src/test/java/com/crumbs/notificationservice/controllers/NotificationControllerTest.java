package com.crumbs.notificationservice.controllers;

import com.crumbs.notificationservice.NotificationServiceApplication;
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
@SpringBootTest(classes = {NotificationServiceApplication.class})
@Transactional
class NotificationControllerTest {

    private MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    @Test
    void testGetAllNotificationsSuccess() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/notifications";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testGetNotificationValidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "382fc8b8-349b-42d9-85f0-c2a47d2bca5f";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/notifications")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testGetNotificationInalidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "382fc8b8-349b-42d9-85f0-c2a47d2bca5x";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/notifications")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testUpdateNotificationInvalidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/notifications";
        String inputJson = "{" +
                "    \"id\": \"382fc8b8-349b-42d9-85f0-c2a47d2bca5f\"," +
                "    \"description\": \"Test Update Notification\"," +
                "    \"is_read\": \"true\"," +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteRecipeInvalidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "382fc8b8-349b-42d9-85f0-c2a47d2bca5x";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/notifications")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteRecipeValidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "382fc8b8-349b-42d9-85f0-c2a47d2bca5f";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/notifications")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.NO_CONTENT.value(), mvcResult.getResponse().getStatus());
    }
}