package com.hiroc.blog_api.aop;


import com.hiroc.blog_api.dto.error.ErrorDetails;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
//        Map<String,String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach(err->{
//            String field = ((FieldError) err).getField();
//            String msg = err.getDefaultMessage();
//            errors.put(field,msg);
//        });

        String message = ex.getBindingResult().getAllErrors().stream()
                .map(err->{
                    String field = (err instanceof FieldError) ? ((FieldError)err).getField():"unknown";
                    String msg = err.getDefaultMessage();
                    return field+" "+msg+". ";
                }).collect(Collectors.joining(" "));
        ErrorDetails errorDetails = new ErrorDetails(message);
        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleDataIntegrityViolationException(DataIntegrityViolationException ex){
        return ResponseEntity.badRequest().body(new ErrorDetails("Conflict with data integrity"));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetails> handleConstraintViolationException(ConstraintViolationException ex){

        String message = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(". "));
        log.debug("error mess {}",message);
        ErrorDetails errorDetails = new ErrorDetails(message);
        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleBadCredentialsException(BadCredentialsException ex){
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDetails> handleRuntimeException(RuntimeException ex){
        log.debug("Advice logging runtime exception: {}",ex.getMessage());
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage());
        return ResponseEntity.internalServerError().body(errorDetails);
    }

}

