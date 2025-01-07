package com.hiroc.blog_api.security;


import com.hiroc.blog_api.domain.Comment;
import com.hiroc.blog_api.domain.Post;
import com.hiroc.blog_api.exceptions.PostNotFoundException;
import com.hiroc.blog_api.exceptions.ResourceNotFoundException;
import com.hiroc.blog_api.repositories.CommentRepository;
import com.hiroc.blog_api.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.stereotype.Component;

@Component("auth")
@RequiredArgsConstructor
public class AuthorizationLogic {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private boolean isAdmin(MethodSecurityExpressionOperations ops){
        return ops.getAuthentication().getAuthorities()
                .stream().anyMatch(auth->auth.getAuthority().equals("ROLE_ADMIN"));
    }

    private String getUsername(MethodSecurityExpressionOperations ops){
        return ops.getAuthentication().getName();
    }

    public boolean isPostOwnerOrAdmin(Integer postId,MethodSecurityExpressionOperations ops){

        if (isAdmin(ops)) return true; //automatic access

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new PostNotFoundException(postId));

        return post.getAuthor().getUsername().equals(getUsername(ops));
    }

    public boolean isCommentOwnerOrAdmin(Integer commentId,MethodSecurityExpressionOperations ops){
        if (isAdmin(ops)) return true;

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(ResourceNotFoundException::new);

        return comment.getAuthor().getUsername().equals(getUsername(ops));
    }

}
