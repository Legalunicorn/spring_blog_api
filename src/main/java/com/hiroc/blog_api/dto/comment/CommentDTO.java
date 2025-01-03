package com.hiroc.blog_api.dto.comment;


import com.hiroc.blog_api.dto.User.UserSummaryDTO;
import com.hiroc.blog_api.dto.post.PostSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Integer id;
    private String body;
    private UserSummaryDTO author;
    private PostSummaryDTO post;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}


//PostDTO -> CommentDTO -> PostSummaryDTO
