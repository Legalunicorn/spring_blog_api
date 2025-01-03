package com.hiroc.blog_api.mappers;


import com.hiroc.blog_api.domain.Post;
import com.hiroc.blog_api.dto.post.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final UserMapper userMapper;
    private final TagMapper tagMapper;
    private final CommentMapper commentMapper;

    public PostDTO map(Post post){
        return PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .createdOn(post.getCreatedOn())
                .updatedOn(post.getUpdatedOn())
                .author(userMapper.toSummary(post.getAuthor()))
                .tags(post.getTags().stream().map(tagMapper::toSummary).collect(Collectors.toSet()))
                .comments(post.getComments().stream().map(commentMapper::map).collect(Collectors.toSet()))
                .build();
    }





}
