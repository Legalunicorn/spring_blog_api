package com.hiroc.blog_api.services;


import com.hiroc.blog_api.domain.LikePost;
import com.hiroc.blog_api.domain.Post;
import com.hiroc.blog_api.domain.Tag;
import com.hiroc.blog_api.domain.User;
import com.hiroc.blog_api.dto.post.PostRequestDTO;
import com.hiroc.blog_api.dto.post.PostUpdateDTO;
import com.hiroc.blog_api.exceptions.PostNotFoundException;
import com.hiroc.blog_api.exceptions.ResourceNotFoundException;
import com.hiroc.blog_api.repositories.LikePostRepository;
import com.hiroc.blog_api.repositories.PostRepository;
import com.hiroc.blog_api.repositories.TagRepository;
import com.hiroc.blog_api.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    private final LikePostRepository likePostRepository;
//    private final PostUpdateMapper postUpdateMapper;

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

    public List<Post> findPublishedPosts(String sort){

        List<Post> published_posts = new ArrayList<>();
        log.debug("Sort is: {}",sort);
        if (sort==null || sort.equals("recent")){
            log.debug("sortig by recent now");
            published_posts= postRepository.getPostByDraftFalseOrderByCreateOnDesc();
            published_posts.forEach(post ->
                    log.debug("Post: id={}, createdOn={}", post.getId(), post.getCreatedOn())
            );
        } else {
            published_posts = postRepository.getPostsByDraftFalseOrderByLikeCount();
        }

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

    //Should Honestly be a tag service method at this point
    private Set<Tag> createPostTags(List<String> request_tags){
        Set<Tag> existingTags = tagRepository.findAllByNameIn(request_tags);
        Map<String,Tag> existingTagsMap = existingTags.stream().collect(Collectors.toMap(Tag::getName,t->t));
        return request_tags.stream()
                .map(name->existingTagsMap.getOrDefault(name,Tag.builder().name(name).build()))
                .collect(Collectors.toSet());
    }

    @Transactional
    public Post createPost(PostRequestDTO postRequest, String username){
        //Get the user from the username
        // WARNING: change this to be a User from context passed by the controller
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username not found:"+username));


        //Optimised approach to avoid n+1 queries
        Set<Tag> existingTags = tagRepository.findAllByNameIn(postRequest.getTags());
        //Map existing tags to the entity
        Map<String, Tag> exitstingTagsMap = existingTags.stream().collect(Collectors.toMap(Tag::getName, t -> t));

        //For each tag: either reference existing tag or create a new tag
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
                .draft(postRequest.getDraft())
                .build();

        return postRepository.save(newPost);
    }

    @Transactional
    //PreAuthorization filter checked the validity of post and authorization
    //DTO DOES NOT have to be valid, it can all be null
    public Post updatePost(PostUpdateDTO postRequest, Integer postId){
        //Fetch the post
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new PostNotFoundException(postId));

        //Set the tags logic, similiar to creating post
        if (postRequest.getTags()!=null){
            Set<Tag> new_tags = createPostTags(postRequest.getTags());
            post.setTags(new_tags);
        }
        if (postRequest.getTitle()!=null) post.setTitle(postRequest.getTitle());
        if (postRequest.getBody()!=null) post.setBody(postRequest.getBody());
        if (postRequest.getThumbnail()!=null) post.setThumbnail(postRequest.getThumbnail());
        if (postRequest.getDraft()!=null) post.setDraft(postRequest.getDraft());

        //Save
        postRepository.save(post);

        //Return the post
        return post;
    }

    @Transactional
    public void deletePostById(Integer id){
        //PreAuthorize has already checked that the post exists
        postRepository.deleteById(id);
    }

    @Transactional
    public void likePost(Integer postId,User user){
        //Check if user has already liked post s
        if (likePostRepository.existsByPostIdAndUserId(postId,user.getId())){
            return; //Don't do anything
        }

        //Check if the post exists
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new PostNotFoundException(postId));
        LikePost like = LikePost.builder()
                .post(post)
                .user(user)
                .build();

        likePostRepository.save(like);

    }

    @Transactional
    public void unlikePost(Integer postId,User user){
        likePostRepository.deleteByPostIdAndUserId(postId,user.getId());
    }



}

