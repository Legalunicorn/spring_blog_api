package com.hiroc.blog_api.controllers;


import com.hiroc.blog_api.domain.Post;
import com.hiroc.blog_api.domain.User;
import com.hiroc.blog_api.dto.User.UserProfileDTO;
import com.hiroc.blog_api.dto.User.UserRequestDTO;
import com.hiroc.blog_api.dto.User.UserSummaryDTO;
import com.hiroc.blog_api.mappers.PostMapper;
import com.hiroc.blog_api.mappers.UserMapper;
import com.hiroc.blog_api.services.PostService;
import com.hiroc.blog_api.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final UserMapper userMapper;
    private final PostMapper postMapper;


    @GetMapping("/{username}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable String username){
        User user = userService.getUser(username);
        Set<Post> posts = postService.findPublishesPostsByAuthorUsername(username);
        UserProfileDTO userProfileDTO = UserProfileDTO
                .builder()
                .posts(posts.stream().map(postMapper::toSummary).collect(Collectors.toSet()))
                .user(userMapper.toSummary(user))
                .build();
        return ResponseEntity.ok(userProfileDTO);
    }

    @PatchMapping("/{username}")
    @PreAuthorize("#userRequestDTO.getUsername()==authentication.principal.username")
    public ResponseEntity<UserSummaryDTO> updateProfile(@Valid @RequestBody UserRequestDTO userRequestDTO){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User updatedUser = userService.updateProfile(userRequestDTO,user);
        log.debug("Updated user: {}",updatedUser.getUsername());
        return ResponseEntity.ok(userMapper.toSummary(updatedUser));
    }

}
