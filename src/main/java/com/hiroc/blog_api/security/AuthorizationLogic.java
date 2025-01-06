package com.hiroc.blog_api.security;


import com.hiroc.blog_api.domain.Post;
import com.hiroc.blog_api.exceptions.PostNotFoundException;
import com.hiroc.blog_api.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.stereotype.Component;

@Component("auth")
@RequiredArgsConstructor
public class AuthorizationLogic {

    private final PostRepository postRepository;

    public boolean isPostOwnerOrAdmin(Integer postId,MethodSecurityExpressionOperations ops){
        boolean isAdmin = ops.getAuthentication().getAuthorities()
                .stream().anyMatch(auth->auth.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) return true; //automatic access

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new PostNotFoundException(postId));

        return post.getAuthor().getUsername().equals(ops.getAuthentication().getName());
    }

}
