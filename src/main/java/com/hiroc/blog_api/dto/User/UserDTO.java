package com.hiroc.blog_api.dto.User;


import com.hiroc.blog_api.dto.comment.CommentDTO;
import com.hiroc.blog_api.dto.post.PostSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer id;
    private String username;
    private Set<CommentDTO> comments;
    private Set<PostSummaryDTO> createdPosts;
    private String profilePicture;


}
