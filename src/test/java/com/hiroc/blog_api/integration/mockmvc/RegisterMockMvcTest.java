package com.hiroc.blog_api.integration.mockmvc;


import com.hiroc.blog_api.dto.authentication.AuthenticationResponse;
import com.hiroc.blog_api.dto.authentication.RegisterRequest;
import com.hiroc.blog_api.services.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.hiroc.blog_api.utils.JsonUtils.toJson;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class RegisterMockMvcTest {

    @MockitoBean
    private AuthenticationService mockAuthenticationService;

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("TEST - Happy flow for Register")
    public void testRegisterHappyFlow() throws Exception {
        RegisterRequest mockRequest = RegisterRequest.builder().username("MOCK").password("MOCK").build();
        AuthenticationResponse response = AuthenticationResponse.builder().token("TOKEN").build();
        given(mockAuthenticationService.register(mockRequest)).willReturn(response);
        MvcResult result = mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(mockRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.token").value("TOKEN"))
                .andReturn();

        log.debug("Register result: {}",result.getResponse().getContentAsString());





    }

}
