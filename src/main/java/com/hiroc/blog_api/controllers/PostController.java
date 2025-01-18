package com.hiroc.blog_api.controllers;


import com.hiroc.blog_api.domain.Post;
import com.hiroc.blog_api.domain.User;
import com.hiroc.blog_api.dto.post.PostDTO;
import com.hiroc.blog_api.dto.post.PostRequestDTO;
import com.hiroc.blog_api.dto.post.PostSummaryDTO;
import com.hiroc.blog_api.dto.post.PostUpdateDTO;
import com.hiroc.blog_api.mappers.PostMapper;
import com.hiroc.blog_api.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;

    //No Authentications

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Integer postId){
        Post post = postService.getPostById(postId);
        return ResponseEntity.ok(postMapper.map(post));
    }

    //tested with post man
    @GetMapping()
    public ResponseEntity<List<PostSummaryDTO>> getAllPublishedPosts(@RequestParam(required=false) String sort){
        List<Post> posts = postService.findPublishedPosts(sort);
        List<PostSummaryDTO> posts_dto = posts.stream().map(postMapper::toSummary).collect(Collectors.toList());
        return ResponseEntity.ok(posts_dto);
    }

    @GetMapping("/users/{username}") //pass postman
    public ResponseEntity<Set<PostSummaryDTO>> getAllPublishedPostsByUser(@PathVariable String username){
        Set<Post> posts = postService.findPublishesPostsByAuthorUsername(username);
        Set<PostSummaryDTO> posts_dto = posts.stream().map(postMapper::toSummary).collect(Collectors.toSet());
        return ResponseEntity.ok(posts_dto);
    }


    //TESTED
    @PostMapping
    public ResponseEntity<PostDTO> createPost(
            @Valid @RequestBody PostRequestDTO request){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("Draft status: {}",request.getDraft());
        if (request.getTags()==null){
            request.setTags(new ArrayList<String>());
        }
        Post post = postService.createPost(request,username);
        PostDTO response =  postMapper.map(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @PutMapping("/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Integer postId){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        postService.likePost(postId,user);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<Void> unlikePost(@PathVariable Integer postId){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        postService.unlikePost(postId,user);
        return ResponseEntity.noContent().build();
    }

    // Self Authentications or Admin, tested with postman
    @GetMapping("/users/{username}/drafted")
    @PreAuthorize("hasRole('ADMIN') || #username==authentication.principal.username")
    public ResponseEntity<Set<PostSummaryDTO>> findDraftedPostsByUser(@PathVariable String username){
        Set<Post> posts = postService.findDraftedPostByUser(username);
        Set<PostSummaryDTO> posts_dto = posts.stream().map(postMapper::toSummary).collect(Collectors.toSet());
        return ResponseEntity.ok(posts_dto);
    }

    @PatchMapping("/{postId}")
    @PreAuthorize("@auth.isPostOwnerOrAdmin(#postId,#root)")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Integer postId,@Valid @RequestBody PostUpdateDTO postUpdateDTO){
        Post post = postService.updatePost(postUpdateDTO,postId);
        PostDTO post_dto = postMapper.map(post);
        return ResponseEntity.ok(post_dto);
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("@auth.isPostOwnerOrAdmin(#postId,#root)") //this also check if the post exists
    public ResponseEntity<Void> deletePostById(@PathVariable Integer postId){
        postService.deletePostById(postId);
        return ResponseEntity.noContent().build();

    }




}

