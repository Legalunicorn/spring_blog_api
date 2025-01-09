package com.hiroc.blog_api.dto.post;


import com.hiroc.blog_api.dto.User.UserSummaryDTO;
import com.hiroc.blog_api.dto.comment.CommentDTO;
import com.hiroc.blog_api.dto.tag.TagSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Integer id;
    private String title;
    private String body;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private String thumbnail;
    private boolean draft;
    //entity references
    private UserSummaryDTO author; //UserDTO -> PostDTO -> UserSummaryDTO
    private Set<TagSummaryDTO> tags;
    private Set<CommentDTO> comments;
    private int like_count;



}
