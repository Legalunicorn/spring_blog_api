package com.hiroc.blog_api.dto.tag;


import com.hiroc.blog_api.dto.post.PostSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {
    private Integer id;
    private String name;
    private Set<PostSummaryDTO> posts;
}
