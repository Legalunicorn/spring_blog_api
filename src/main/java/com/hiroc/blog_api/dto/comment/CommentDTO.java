package com.hiroc.blog_api.dto.comment;


import com.hiroc.blog_api.dto.User.UserSummaryDTO;
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
//    private PostSummaryDTO post; //Not much sense? you only need to link to the post id AT MOST
    private Integer postId;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}


//PostDTO -> CommentDTO -> PostSummaryDTO
