package com.hiroc.blog_api.mappers;


import com.hiroc.blog_api.domain.Post;
import com.hiroc.blog_api.dto.post.PostRequestDTO;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

public interface PostUpdateMapper {
    @Mapping(target="tags",ignore=true)
    void postUpdateRequestToEntity(@MappingTarget Post target, PostRequestDTO source );
    /*
    UPDATABLE: (same as create post)
    - title
    - body
    - thumbnail
    - draft
    - "tags"?

    - We have to handle "tags" on our own ,just like creating post
    - We have to handle the "id" on our own


     */
}
