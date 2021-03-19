package com.crumbs.userservice.controllers;

import com.crumbs.userservice.UserServiceApplication;
import com.crumbs.userservice.config.WebSecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = WebSecurityConfig.class)
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {UserServiceApplication.class})
@Transactional
class UserControllerTest {

    private MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Test
    void testLoginSuccess() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/login";
        String inputJson = "{" +
                "\"username\": \"lmehmedagi\"," +
                "\"password\": \"Password123\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testLoginFailIncorrectPassword() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/login";
        String inputJson = "{" +
                "\"username\": \"lmehmedagi\"," +
                "\"password\": \"pass\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertAll(
                () -> assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus()),
                () -> assertEquals("Password is incorrect", mvcResult.getResponse().getErrorMessage()));
    }

    @Test
    void testLoginFailIncorrectUsername() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/login";
        String inputJson = "{" +
                "\"username\": \"lmehmedagiiii\"," +
                "\"password\": \"password\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus()),
                () -> assertEquals("User with this username does not exist", mvcResult.getResponse().getErrorMessage()));
    }

    @Test
    void testRegisterSuccess() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/register";
        String inputJson = "{" +
                "    \"email\": \"test@gmail.com\"," +
                "    \"username\": \"testtest\"," +
                "    \"firstName\": \"test\"," +
                "    \"lastName\": \"test\"," +
                "    \"gender\": \"female\"," +
                "    \"password\": \"Password123\"," +
                "    \"phoneNumber\": \"062175175\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testRegisterFailMissingField() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/register";
        String inputJson = "{" +
                "    \"email\": \"test@gmail.com\"," +
                "    \"firstName\": \"test\"," +
                "    \"lastName\": \"test\"," +
                "    \"gender\": \"female\"," +
                "    \"password\": \"pass\"," +
                "    \"phoneNumber\": \"062175175\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testRegisterFailInvalidEmail() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/register";
        String inputJson = "{" +
                "    \"email\": \"gmail.com\"," +
                "    \"username\": \"test\"," +
                "    \"firstName\": \"test\"," +
                "    \"lastName\": \"test\"," +
                "    \"gender\": \"female\"," +
                "    \"password\": \"Password123\"," +
                "    \"phoneNumber\": \"062175175\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testRegisterFailUserAlreadyExists() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/register";
        String inputJson = "{" +
                "    \"email\": \"test@gmail.com\"," +
                "    \"username\": \"lmehmedagi\"," +
                "    \"firstName\": \"test\"," +
                "    \"lastName\": \"test\"," +
                "    \"gender\": \"female\"," +
                "    \"password\": \"Password123\"," +
                "    \"phoneNumber\": \"062175175\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.CONFLICT.value(), mvcResult.getResponse().getStatus());
    }
}
