package com.hiroc.blog_api.mappers;


import com.hiroc.blog_api.domain.Post;
import com.hiroc.blog_api.dto.comment.CommentDTO;
import com.hiroc.blog_api.dto.post.PostDTO;
import com.hiroc.blog_api.dto.tag.TagSummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final UserMapper userMapper;
    private final TagMapper tagMapper;
    private final CommentMapper commentMapper;

    public PostDTO map(Post post){

        PostDTO postDTO =  PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .createdOn(post.getCreatedOn())
                .updatedOn(post.getUpdatedOn())
                .author(userMapper.toSummary(post.getAuthor()))
//                .tags(post.getTags().stream().map(tagMapper::toSummary).collect(Collectors.toSet()))
//                .comments(post.getComments().stream().map(commentMapper::map).collect(Collectors.toSet()))
                .build();

        if (post.getTags()!=null){
            postDTO.setTags(post.getTags().stream().map(tagMapper::toSummary).collect(Collectors.toSet()));
        }else{
            postDTO.setTags(new HashSet<TagSummaryDTO>());
        }
        if (post.getComments()!=null){
            postDTO.setComments(post.getComments().stream().map(commentMapper::map).collect(Collectors.toSet()));
        }else{
            postDTO.setComments(new HashSet<CommentDTO>());
        }
        return postDTO;
    }





}
