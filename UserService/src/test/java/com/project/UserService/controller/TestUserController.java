package com.project.UserService.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Commons.UserIdentifier;
import com.project.UserService.dto.CreateUserRequestDTO;
import com.project.UserService.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {UserController.class})
public class TestUserController {

    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(UserController.class).build();
    }

    public CreateUserRequestDTO createUserRequestDTO() {
        CreateUserRequestDTO createUserRequestDTO = CreateUserRequestDTO.builder().
                email("test@gmail.com").
                contact("1234567890").
                userIdentifier(UserIdentifier.PAN).
                userIdentifierValue("ABCD1234").
                name("Test").
                build();

        return createUserRequestDTO;
    }
    @Test
    public void testUserCreation() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/create").
                content(new ObjectMapper().writeValueAsString(createUserRequestDTO())).
                contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Assert.assertNotNull(result);
    }
}
