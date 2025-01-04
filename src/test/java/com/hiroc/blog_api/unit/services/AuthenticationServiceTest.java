package com.hiroc.blog_api.unit.services;


import com.hiroc.blog_api.domain.Role;
import com.hiroc.blog_api.domain.User;
import com.hiroc.blog_api.dto.authentication.AuthenticationResponse;
import com.hiroc.blog_api.dto.authentication.RegisterRequest;
import com.hiroc.blog_api.repositories.UserRepository;
import com.hiroc.blog_api.services.AuthenticationService;
import com.hiroc.blog_api.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private final String username="MOCK";
    private final String password="MOCK";

    @BeforeEach
    public void setUp(){
        User user = User
                .builder()
                .username(username)
                .password(password)
                .role(Role.USER)
                .build();

        String token = "MOCK_TOKEN";
        given(passwordEncoder.encode(any(String.class))).willReturn(password);
        given(jwtService.generateToken(any(UserDetails.class))).willReturn(token);
    }

    @Test
    public void registerServiceHappyFlow(){
//        given(passwordEncoder.encode(password)).willReturn(password);
        RegisterRequest request = RegisterRequest.builder().username(username).password(password).build();
        AuthenticationResponse  response = authenticationService.register(request);

        assertNotNull(response.getToken());


    }



}
