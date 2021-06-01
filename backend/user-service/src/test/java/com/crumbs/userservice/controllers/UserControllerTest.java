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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = WebSecurityConfig.class)
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {UserServiceApplication.class})
@Transactional
class UserControllerTest {

    private MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    /* Could not be recognized since its not in Controller. Will be fixed
    @Test
    void testLoginSuccess() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/account/login";
        String inputJson = "{\n" +
                "    \"username\": \"lmehmedagi\",\n" +
                "    \"password\": \"Password123!\"\n" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }


    @Test
    void testLoginFailIncorrectPassword() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/account/login";
        String inputJson = "{\n" +
                "    \"username\": \"lmehmedagi\",\n" +
                "    \"password\": \"Passw!\"\n" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    } */

    @Test
    void testLoginFailIncorrectUsername() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/auth/login";
        String inputJson = "{" +
                "\"username\": \"lmehmedagiiii\"," +
                "\"password\": \"password\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testRegisterSuccess() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/auth/register";
        String inputJson = "{" +
                "    \"email\": \"test@gmail.com\"," +
                "    \"username\": \"testtest\"," +
                "    \"first_name\": \"test\"," +
                "    \"last_name\": \"test\"," +
                "    \"gender\": \"female\"," +
                "    \"password\": \"Password123!\"," +
                "    \"phone_number\": \"062175175\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testRegisterFailMissingField() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/auth/register";
        String inputJson = "{" +
                "    \"email\": \"test@gmail.com\"," +
                "    \"first_name\": \"test\"," +
                "    \"last_name\": \"test\"," +
                "    \"gender\": \"female\"," +
                "    \"password\": \"pass\"," +
                "    \"phone_number\": \"062175175\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testRegisterFailInvalidEmail() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/auth/register";
        String inputJson = "{" +
                "    \"email\": \"gmail.com\"," +
                "    \"username\": \"test\"," +
                "    \"first_name\": \"test\"," +
                "    \"last_name\": \"test\"," +
                "    \"gender\": \"female\"," +
                "    \"password\": \"Password123\"," +
                "    \"phone_number\": \"062175175\"" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testRegisterFailUserAlreadyExists() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/auth/register";
        String inputJson = "{\n" +
                "  \"email\": \"lejla@gmail.com\",\n" +
                "  \"first_name\": \"Lela\",\n" +
                "  \"gender\": \"female\",\n" +
                "  \"last_name\": \"Mehmo\",\n" +
                "  \"password\": \"Password123!\",\n" +
                "  \"phone_number\": \"0603189774\",\n" +
                "  \"username\": \"lmehmedagi\"\n" +
                "}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        assertEquals(HttpStatus.CONFLICT.value(), mvcResult.getResponse().getStatus());
    }
}
