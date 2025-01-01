package com.hiroc.blog_api.unit.controllers;


import com.hiroc.blog_api.config.JwtAuthenticationFilter;
import com.hiroc.blog_api.config.SecurityConfig;
import com.hiroc.blog_api.controllers.AuthenticationController;
import com.hiroc.blog_api.dto.authentication.AuthenticationResponse;
import com.hiroc.blog_api.dto.authentication.RegisterRequest;
import com.hiroc.blog_api.services.AuthenticationService;
import com.hiroc.blog_api.services.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.hiroc.blog_api.utils.JsonUtils.toJson;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@Slf4j
@Import(SecurityConfig.class)
public class AuthenticationControllerTest {


    private final String mockToken = "TOKEN";

    @MockitoBean
    private AuthenticationService mockAuthenticationService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private AuthenticationProvider authenticationProvider;


    @Autowired
    private MockMvc mvc;


    @Test
    @DisplayName("TEST - Happy flow for Register")
    public void testRegisterHappyFlow() throws Exception {
        RegisterRequest mockRequest = RegisterRequest.builder().username("MOCK").password("MOCK").build();
        AuthenticationResponse mockResponse = AuthenticationResponse.builder().token(mockToken).build();
        given(mockAuthenticationService.register(mockRequest)).willReturn(mockResponse);


        log.debug("JSON REQUEST: {}",toJson(mockRequest));

        //Run test
        mvc.perform(post("/api/auth/register")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(mockRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.token").value(mockToken))
                .andReturn();

    }

}
