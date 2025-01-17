package com.hiroc.blog_api.mappers;


import com.hiroc.blog_api.domain.Post;
import com.hiroc.blog_api.dto.comment.CommentDTO;
import com.hiroc.blog_api.dto.post.PostDTO;
import com.hiroc.blog_api.dto.post.PostSummaryDTO;
import com.hiroc.blog_api.dto.tag.TagSummaryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostMapper {

    private final UserMapper userMapper;
    private final TagMapper tagMapper;
    private final CommentMapper commentMapper;

    public  PostDTO map(Post post){


        PostDTO postDTO =  PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .createdOn(post.getCreatedOn())
                .updatedOn(post.getUpdatedOn())
                .author(userMapper.toSummary(post.getAuthor()))
                .draft(post.isDraft())
                .thumbnail(post.getThumbnail())
                .build();

        if (post.getLikes()==null){
            postDTO.setLike_count(0);
        } else{
            postDTO.setLike_count(post.getLikes().size());
        }

        if (post.getTags()!=null){
            log.debug("Mapping {} tags for the post: ",post.getTags().size());
            postDTO.setTags(post.getTags().stream().map(tagMapper::toSummary).collect(Collectors.toSet()));
        }else{
            log.debug("No tags found for the post");
            postDTO.setTags(new HashSet<TagSummaryDTO>());
        }
        if (post.getComments()!=null){
            postDTO.setComments(post.getComments().stream().map(commentMapper::map).collect(Collectors.toSet()));
        }else{
            postDTO.setComments(new HashSet<CommentDTO>());
        }
        return postDTO;
    }

    public  PostSummaryDTO toSummary(Post post){
        PostSummaryDTO postSummary = PostSummaryDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .draft(post.isDraft())
                .createdOn(post.getCreatedOn())
                .updatedOn(post.getUpdatedOn())
                .author(userMapper.toSummary(post.getAuthor()))
                .tags(post.getTags().stream().map(tagMapper::toSummary).collect(Collectors.toSet()))
                .like_count(post.getLikes().size())
                .comment_count(post.getComments().size())
                .thumbnail(post.getThumbnail())
                .build();

        return postSummary;
    }





}
