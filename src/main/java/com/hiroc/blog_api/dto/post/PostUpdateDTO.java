package com.hiroc.blog_api.dto.post;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateDTO {
    @Size(min=1,max=300)
    private String title;

    @Size(min=1,max=50000)
    private String body;
    private String thumbnail; //Can be null
    private Boolean draft;

    private List<String> tags;
}
