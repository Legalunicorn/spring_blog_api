package com.hiroc.blog_api.mappers;

import com.hiroc.blog_api.domain.Comment;
import com.hiroc.blog_api.dto.comment.CommentDTO;
import com.hiroc.blog_api.dto.comment.CommentSummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final UserMapper userMapper;
    private final PostMapper postMapper;

    public CommentDTO map(Comment comment){
        return CommentDTO.builder()
                .id(comment.getId())
                .body(comment.getBody())
                .post()
                .author(userMapper.toSummary(comment.getAuthor()))
                .createdOn(comment.getCreatedOn())
                .build();
    }

    public CommentSummaryDTO toSummary(Comment comment){
        return CommentSummaryDTO.builder()
                .id(comment.getId())
                .body(comment.getBody())
                .createdOn((comment.getCreatedOn()))
                .build();
    }

}

