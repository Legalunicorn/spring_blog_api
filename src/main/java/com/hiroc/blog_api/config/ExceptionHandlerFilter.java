package com.hiroc.blog_api.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiroc.blog_api.dto.error.ErrorDetails;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        try{
            filterChain.doFilter(request,response);
        } catch (JwtException e){
            ErrorDetails details = setErrorResponse(HttpStatus.BAD_REQUEST,response,e);
            response.getWriter().write(convertObjectToJson(details));
        }
    }

    public ErrorDetails setErrorResponse(HttpStatus status,HttpServletResponse response , Throwable ex){
        response.setStatus(status.value());
        response.setContentType("application/json");
        return ErrorDetails
                .builder()
                .message(ex.getMessage())
                .code("JWT_ERROR")
                .build();

    }

    public String convertObjectToJson(Object object) throws JsonProcessingException{
        if (object == null) return null;
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);

    }

}
