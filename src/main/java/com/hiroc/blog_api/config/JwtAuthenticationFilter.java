package com.hiroc.blog_api.config;

import com.hiroc.blog_api.services.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Extract the JWT from the request
        final String authHeader = request.getHeader("Authorization");
        log.debug("Headers: {}",request.getHeaderNames());
        if (authHeader==null || !authHeader.startsWith("Bearer")){
            //No JWT found
            log.debug("No JWT found");
            filterChain.doFilter(request,response);
            return;
        }

        final String jwt = authHeader.substring(7);
        log.info("Extract request JWT: {}",jwt);
        String username = null;
        try{
            username = jwtService.extractUsername(jwt);
            log.debug("extracted username: {}",username);
        } catch(Exception e){
            //Send an exception
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"JWT Token is invalid/expired. Please login again");
            log.warn("JWT Token has expired or is invalid {} ",e.getMessage());
            throw new JwtException("JWT Token expired/invalid. Please login again");
        }
//        final String username = jwtService.extractUsername(jwt);
        if (username !=null && SecurityContextHolder.getContext().getAuthentication()==null){
            //User is not stored in the SecurityContext
            //We have a non null username (valid) with us
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(jwt,userDetails)){
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(token);

            }
            //Move along the next filter
            filterChain.doFilter(request,response);
        }


    }


}
