package com.hiroc.blog_api.controllers;


import com.hiroc.blog_api.dto.authentication.AuthenticationResponse;
import com.hiroc.blog_api.dto.authentication.LoginRequest;
import com.hiroc.blog_api.dto.authentication.RegisterRequest;
import com.hiroc.blog_api.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody LoginRequest request
    ){
        AuthenticationResponse response = authenticationService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request)
    {
        log.info("Registering from controller" );
        return ResponseEntity.ok(authenticationService.register(request));
    }
}
