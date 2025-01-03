package com.hiroc.blog_api.dto.post;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDTO {
    private String title;
    private String body;
    private String thumbnail;
    private List<String> tags;

    /*
    the user is not needed?
    the entity would get the user from the Security Context
     */
}
