package com.hiroc.blog_api.unit.services;


import com.hiroc.blog_api.domain.Role;
import com.hiroc.blog_api.domain.User;
import com.hiroc.blog_api.services.JwtService;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private final String TEST_USERNAME="TEST_USERNAME";
    private UserDetails mockUserDetails;

    @BeforeEach
    public void setUp(){
        String MOCK_SECRET_KEY = "208676bb36371a3a2a3a6621a40d94eaa80d99575b3c73fc9ac6674f2ddab8c07bf61ff26a6a865217d4d0c5c80b2141c2a197267fe751883a97d372d3437f112cbe48b8e764687fc09d91641df936a3a906412c3df43a09fad1511fcb3a243608250e003f0f504570899bfe45074a805a6080862f9564ba6f0ab0a985fd5854ce0488b9dfbc4ab80320655918fb416d37d82f5b10b1a1fb5a1279184aec35af85d90a07da9282df8ac6b2657a1476fac6e32ac4f8da8d79391aed72fda93441f491bbb40c187e14f26db0ebc002b7ee94986f247c253094cb6286e9250e7cbe7fb83f5927277d2f9ca16b29439c3acb11fcfd1b375ababdad1d3b724b5d5e71";
        ReflectionTestUtils.setField(jwtService,"SECRET_KEY", MOCK_SECRET_KEY);
        mockUserDetails = User.builder()
                .username(TEST_USERNAME)
                .password("TEST_PASSWORD")
                .role(Role.USER)
                .build();
    }


    @Test
    @DisplayName("Test for valid token creation")
    public void generateTokenHappyFlow(){
        String token = jwtService.generateToken(mockUserDetails);

        assertNotNull(token);
        assertEquals(TEST_USERNAME,jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token,mockUserDetails));
    }

    @Test
    @DisplayName("Test isValidToken()")
    public void testValidToken(){
        String validToken = jwtService.generateToken(mockUserDetails);
        String invalidToken = Jwts
                .builder()
                .claims(new HashMap<String,Object>())
                .subject(TEST_USERNAME)
                .issuedAt(new Date(System.currentTimeMillis()-2))
                .expiration(new Date(System.currentTimeMillis()-1))
                .signWith(jwtService.getSignInKey())
                .compact();

        assertTrue(jwtService.isTokenValid(validToken,mockUserDetails));
        assertFalse(jwtService.isTokenValid(invalidToken,mockUserDetails));
    }


}
