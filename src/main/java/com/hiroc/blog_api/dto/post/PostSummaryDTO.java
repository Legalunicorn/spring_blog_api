package com.hiroc.blog_api.dto.post;


import com.hiroc.blog_api.dto.User.UserSummaryDTO;
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
    private boolean draft;
    private UserSummaryDTO author;
    //missing tags and comments?..



    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
