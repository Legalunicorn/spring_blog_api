package com.hiroc.blog_api.dto.post;


import com.hiroc.blog_api.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostSummaryDTO {
    private Integer id;
    private String title;
    private String body;
    private User author;
//    private Set<Tag> tags;
//    private Set<Comment> comments;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
