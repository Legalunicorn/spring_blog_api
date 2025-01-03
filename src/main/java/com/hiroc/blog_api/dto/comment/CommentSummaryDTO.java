package com.hiroc.blog_api.dto.comment;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentSummaryDTO {
    private Integer id;
    private String body;
    private LocalDateTime createdOn;
//    private LocalDateTime updatedOn;

}
