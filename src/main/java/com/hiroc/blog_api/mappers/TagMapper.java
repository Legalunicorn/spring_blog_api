package com.hiroc.blog_api.mappers;

import com.hiroc.blog_api.domain.Tag;
import com.hiroc.blog_api.dto.tag.TagSummaryDTO;

public class TagMapper {

    public TagSummaryDTO toSummary(Tag tag){
        return TagSummaryDTO
                .builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

//    public Set<TagSummaryDTO> toSummaryList(List<Tag> tags){
//        return tags.stream().map(this::toSummary).collect(Collectors.toSet());
//    }

}
