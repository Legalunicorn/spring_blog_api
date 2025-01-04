package com.hiroc.blog_api.dto.comment;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
//even though its only one field, I just use it for validation
public class CommentRequestDTO {
    @NotNull
    @Size(max=500)
    private String body;

    @NotNull
    @JsonProperty("post_id")
    private Integer postId;
}

