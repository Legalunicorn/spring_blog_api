package com.hiroc.blog_api.services;


import com.hiroc.blog_api.domain.Comment;
import com.hiroc.blog_api.domain.Post;
import com.hiroc.blog_api.domain.User;
import com.hiroc.blog_api.dto.comment.CommentRequestDTO;
import com.hiroc.blog_api.exceptions.PostNotFoundException;
import com.hiroc.blog_api.repositories.CommentRepository;
import com.hiroc.blog_api.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public Comment createComment(CommentRequestDTO request, User author){

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(()-> new PostNotFoundException(request.getPostId()));


        Comment comment = Comment.builder()
                .body(request.getBody())
                .author(author)
                .post(post)
                .build();

        //

        return commentRepository.save(comment);
    }


}
