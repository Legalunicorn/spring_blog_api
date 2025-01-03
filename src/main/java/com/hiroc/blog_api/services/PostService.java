package com.hiroc.blog_api.services;


import com.hiroc.blog_api.domain.Post;
import com.hiroc.blog_api.domain.Tag;
import com.hiroc.blog_api.domain.User;
import com.hiroc.blog_api.dto.post.PostDTO;
import com.hiroc.blog_api.dto.post.PostRequestDTO;
import com.hiroc.blog_api.exceptions.ResourceNotFoundException;
import com.hiroc.blog_api.mappers.PostMapper;
import com.hiroc.blog_api.repositories.PostRepository;
import com.hiroc.blog_api.repositories.TagRepository;
import com.hiroc.blog_api.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {


    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final PostMapper postMapper;

    public PostDTO getPostById(Integer id){
        Post post = postRepository.getPostById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post not found with id: "+id));

        return postMapper.map(post);
    }

    @Transactional
    public void createPost(PostRequestDTO postRequest, String username){
        //Get the user from the username
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username not found:"+username));

        //Get a set from the request
        //Optimise next time, this is doing n queries
        Set<Tag> tags = postRequest.getTags().stream()
                .map(name->tagRepository.findByName(name)
                        .orElseGet(()-> Tag.builder().name(name).build()
                )).collect(Collectors.toSet());

        //Create the Post Entity
        Post newPost = Post.builder()
                .title(postRequest.getTitle())
                .body(postRequest.getBody())
                .author(user)
                .thumbnail(postRequest.getThumbnail())
                .tags(tags)
                .build();

        postRepository.save(newPost);
    }



}
