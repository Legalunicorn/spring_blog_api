package com.hiroc.blog_api.dto.post;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Size(min=1,max=150)
    private String title;
    @Size(min=1,max=50000)
    private String body;
    private String thumbnail;
    private List<String> tags;
    @NotNull
    private Boolean draft;

    /*
    the user is not needed?
    the entity would get the user from the Security Context
     */
}
