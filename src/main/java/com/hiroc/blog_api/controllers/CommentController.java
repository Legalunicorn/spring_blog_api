package com.hiroc.blog_api.controllers;

import com.hiroc.blog_api.domain.Comment;
import com.hiroc.blog_api.domain.User;
import com.hiroc.blog_api.dto.comment.CommentDTO;
import com.hiroc.blog_api.dto.comment.CommentRequestDTO;
import com.hiroc.blog_api.mappers.CommentMapper;
import com.hiroc.blog_api.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CommentRequestDTO request){
        User author = (User) SecurityContextHolder.getContext().getAuthentication();

        Comment comment = commentService.createComment(request,author);
        CommentDTO response = commentMapper.map(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

}
