package com.hiroc.blog_api.services;


import com.hiroc.blog_api.domain.Role;
import com.hiroc.blog_api.domain.User;
import com.hiroc.blog_api.dto.authentication.AuthenticationResponse;
import com.hiroc.blog_api.dto.authentication.LoginRequest;
import com.hiroc.blog_api.dto.authentication.RegisterRequest;
import com.hiroc.blog_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public final AuthenticationResponse register(RegisterRequest request){
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        log.debug("The register token is {}",token);
        return AuthenticationResponse.builder()
                .token(token)
                .build();

    }

    public final AuthenticationResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        log.info("User has been authentication past login");
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        String token = jwtService.generateToken(user);
        log.debug("The login token is {}",token);
        return new AuthenticationResponse(token);
    }


}