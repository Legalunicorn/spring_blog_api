package com.hiroc.blog_api.controllers;


import com.hiroc.blog_api.domain.Post;
import com.hiroc.blog_api.dto.post.PostDTO;
import com.hiroc.blog_api.dto.post.PostRequestDTO;
import com.hiroc.blog_api.mappers.PostMapper;
import com.hiroc.blog_api.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Integer postId){
        PostDTO response = postService.getPostById(postId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(
            @Valid @RequestBody PostRequestDTO request){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (request.getTags()==null){
            request.setTags(new ArrayList<String>());
        }
        Post post = postService.createPost(request,username);
        PostDTO response = postMapper.map(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}

