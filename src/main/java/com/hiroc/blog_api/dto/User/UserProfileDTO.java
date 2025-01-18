package com.hiroc.blog_api.dto.User;


import com.hiroc.blog_api.dto.post.PostSummaryDTO;
import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private UserSummaryDTO user;
    private Set<PostSummaryDTO> posts;
}
