package com.hiroc.blog_api.services;


import com.hiroc.blog_api.domain.Post;
import com.hiroc.blog_api.domain.Tag;
import com.hiroc.blog_api.domain.User;
import com.hiroc.blog_api.dto.post.PostRequestDTO;
import com.hiroc.blog_api.exceptions.ResourceNotFoundException;
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

    public Post getPostById(Integer id){
        log.debug("fetching post with id: {}",id);
        Post post = postRepository.getPostById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post not found with id: "+id));

//        post.getTags().forEach(tag->log.info("tag fetch along with post: {}",tag));
//        log.debug("The retrieved title: {}  and tags size: {}",post.getTitle(),post.getTags().size());

        return post;
    }

    public Set<Post> findPublishesPostsByAuthorUsername(String username){
        //check the author is a valid person
        userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("user not found: "+username));

        //get all the post
        Set<Post> posts = postRepository.findPublishedPostsByAuthorUsername(username);
        return posts;
    }

    public Set<Post> findPublishedPosts(){
        Set<Post> published_posts = postRepository.getPostByDraftFalse();
        log.debug("{} published posts fetched",published_posts.size());
        return published_posts;
    }

    public Set<Post> findDraftedPostByUser(String username){
        //First check the user exists
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("No such user: "+username));

        Set<Post> posts = postRepository.findPostsByDraftTrueAndUsernameEquals(username);
        log.debug("Drafted post by user: {} size of {}",username,posts.size());
        return posts;

    }

    public Tag findPostsByTag(String tag_name){
        Tag tag = tagRepository.findTagWithPosts(tag_name);
        log.debug("tag null: {}",tag==null);
        return tag;
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
                .draft(postRequest.isDraft())
                .build();

        return postRepository.save(newPost);
    }

    public void deletePostById(Integer id){
        //PreAuthorize has already checked that the post exists
        postRepository.deleteById(id);
    }



}
