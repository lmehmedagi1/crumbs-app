package com.crumbs.notificationservice.controllers;

import com.crumbs.notificationservice.NotificationServiceApplication;
import com.crumbs.notificationservice.requests.NotificationRequest;
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
import org.springframework.web.reactive.function.BodyInserters;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {NotificationServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureWebTestClient
//@WebFluxTest(MyControllerTest.class)
class NotificationControllerTest {

    private MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testGetNotificationValidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "382fc8b8-349b-42d9-85f0-c2a47d2bca5f";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/notifications")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testGetNotificationForUserId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "d913320a-baf1-43e0-b8b7-25f748e574ee";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/notifications")
                .param("userId", id)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }


    @Test
    void testGetNotificationsForUserIdSuccess() throws Exception {
        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/notifications")
                                .queryParam("userId", "d913320a-baf1-43e0-b8b7-25f748e574ee")
                                .build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
//                .expectHeader()
//                .contentType(APPLICATION_JSON)
//                .expectBody()
//                .jsonPath("$.length()").isEqualTo(3)
//                .jsonPath("$[0].id").isEqualTo(1)
//                .jsonPath("$[0].name").isEqualTo("duke")
//                .jsonPath("$[0].tags").isNotEmpty();
    }

    @Test
    void testGetNotificationsForUserIdFail() throws Exception {
        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/notifications")
                                .queryParam("userId", "d913320a-baf1-43e0-b8b7-25f748e573ee")
                                .build())
                .exchange()
                .expectStatus()
                .is5xxServerError();

    }

//
//    @Test
//    void testCreateNotificationForUserIdSuccess() throws Exception {
//        NotificationRequest nr = new NotificationRequest("d913320a-baf1-43e0-b8b7-25f748e574ee", "Test create notif", true);
//        webTestClient
//                .post()
//                .uri(uriBuilder ->
//                        uriBuilder
//                                .path("/notifications")
//                                .queryParam("userId", "d913320a-baf1-43e0-b8b7-25f748e574ee", "token")
//                                .build()).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(nr))
//                .exchange()
//                .expectStatus()
//                .is2xxSuccessful();
//    }
//
//
//    @Test
//    void testCreateNotificationForUserIdFail() throws Exception {
//        NotificationRequest nr = new NotificationRequest("d913320a-baf1-43e0-b8b7-25f748e572ee", "Test create notif", true);
//        webTestClient
//                .post()
//                .uri(uriBuilder ->
//                        uriBuilder
//                                .path("/notifications")
//                                .queryParam("userId", "d913320a-baf1-43e0-b8b7-25f748e574ee", "token")
//                                .build()).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(nr))
//                .exchange()
//                .expectStatus()
//                .is5xxServerError();
//    }


    @Test
    void testUpdateNotificationMarkAllReadForUserIdSuccess() throws Exception {
        webTestClient
                .post()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/notifications/update")
                                .queryParam("userId", "d913320a-baf1-43e0-b8b7-25f748e574ee", "token")
                                .build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

    @Test
    void testUpdateNotificationMarkAllReadForUserIdFail() throws Exception {
        webTestClient
                .post()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/notifications/update")
                                .queryParam("userId", "d913320a-baf1-43e0-b8b7-25f748e573ee", "token")
                                .build())
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }


    @Test
    void testGetNotificationInvalidId() throws Exception {
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
    void deleteNotificationInvalidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "382fc8b8-349b-42d9-85f0-c2a47d2bca5x";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/notifications")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteNotificationValidId() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String id = "382fc8b8-349b-42d9-85f0-c2a47d2bca5f";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/notifications")
                .param("id", id)).andReturn();

        assertEquals(HttpStatus.NO_CONTENT.value(), mvcResult.getResponse().getStatus());
    }
}