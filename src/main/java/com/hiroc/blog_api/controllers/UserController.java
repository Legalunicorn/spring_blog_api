package com.hiroc.blog_api.controllers;


import com.hiroc.blog_api.domain.User;
import com.hiroc.blog_api.dto.User.UserRequestDTO;
import com.hiroc.blog_api.dto.User.UserSummaryDTO;
import com.hiroc.blog_api.mappers.UserMapper;
import com.hiroc.blog_api.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;


    @PatchMapping("/user/{username}")
    @PreAuthorize("#userRequestDTO.getUsername()==authentication.principal.username")
    public ResponseEntity<UserSummaryDTO> updateProfile(@Valid @RequestBody UserRequestDTO userRequestDTO){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User updatedUser = userService.updateProfile(userRequestDTO,user);
        log.debug("Updated user: {}",updatedUser.getUsername());
        return ResponseEntity.ok(userMapper.toSummary(updatedUser));
    }

}
