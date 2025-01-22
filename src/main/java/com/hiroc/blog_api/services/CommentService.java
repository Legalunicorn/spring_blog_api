package com.hiroc.blog_api.services;


import com.hiroc.blog_api.domain.Comment;
import com.hiroc.blog_api.domain.Post;
import com.hiroc.blog_api.domain.User;
import com.hiroc.blog_api.dto.comment.CommentRequestDTO;
import com.hiroc.blog_api.exceptions.PostNotFoundException;
import com.hiroc.blog_api.repositories.CommentRepository;
import com.hiroc.blog_api.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public Comment createComment(CommentRequestDTO request, User author){

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(()-> new PostNotFoundException(request.getPostId()));

        log.debug("The comment post id is {}",post.getId());

        Comment comment = Comment.builder()
                .body(request.getBody())
                .author(author)
                .post(post)
                .build();

        //comment is the owning side?
        return commentRepository.save(comment);
    }

    public void deleteComment(Integer commentId){
        //@PreAuthorize has confirmed that the post exists
        commentRepository.deleteById(commentId);

    }


}
