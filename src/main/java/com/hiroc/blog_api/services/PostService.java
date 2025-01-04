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

import java.util.Map;
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
        log.info("fetching post with id: {}",id);
        Post post = postRepository.getPostById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post not found with id: "+id));

        post.getTags().forEach(tag->log.info("tag fetch along with post: {}",tag));
        log.info("The retrieved title: {}  and tags size: {}",post.getTitle(),post.getTags().size());

        return postMapper.map(post);
    }

    @Transactional
    public Post createPost(PostRequestDTO postRequest, String username){
        //Get the user from the username
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username not found:"+username));


        //Optimised approach to avoid n+1 queries
        Set<Tag> existingTags = tagRepository.findAllByNameIn(postRequest.getTags());
        //Map name to Tag
        Map<String, Tag> exitstingTagsMap = existingTags.stream().collect(Collectors.toMap(Tag::getName, t -> t));
        Set<Tag> tags = postRequest.getTags().stream()
                .map(name -> exitstingTagsMap.getOrDefault(name, Tag.builder().name(name).build()))
                .collect(Collectors.toSet());

        //Create the Post Entity
        Post newPost = Post.builder()
                .title(postRequest.getTitle())
                .body(postRequest.getBody())
                .author(user)
                .thumbnail(postRequest.getThumbnail())
                .tags(tags)
                .build();

        return postRepository.save(newPost);
    }



}
