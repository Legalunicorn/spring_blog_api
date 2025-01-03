package com.hiroc.blog_api.services;


import com.hiroc.blog_api.domain.Post;
import com.hiroc.blog_api.dto.post.PostDTO;
import com.hiroc.blog_api.dto.post.PostResponse;
import com.hiroc.blog_api.exceptions.ResourceNotFoundException;
import com.hiroc.blog_api.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {


    private final PostRepository postRepository;

    public PostDTO getPostById(Integer id){
        Post post = postRepository.getPostById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post not found with id: "+id));

        return PostResponse.builder()
                .id(post.getId())
                .tags(post.getTags())
                .comments(post.getComments())
                .author(post.getAuthor())
                .createdOn(post.getCreatedOn())
                .updatedOn(post.getUpdatedOn())
                .build();
    }



}
