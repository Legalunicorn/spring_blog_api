package com.hiroc.blog_api.mappers;

import com.hiroc.blog_api.domain.Post;
import com.hiroc.blog_api.domain.Tag;
import com.hiroc.blog_api.dto.post.PostSummaryDTO;
import com.hiroc.blog_api.dto.tag.TagDTO;
import com.hiroc.blog_api.dto.tag.TagSummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class TagMapper {

    //Im not sure if I need DI here?
    // Or should I just import class and method?..
//    private final PostMapper postMapper;

    private final UserMapper userMapper;

    public  TagSummaryDTO toSummary(Tag tag){
        return TagSummaryDTO
                .builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

    public  TagDTO map(Tag tag){
        return TagDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .posts(tag.getPosts().stream().map(this::PostToSummary).collect(Collectors.toSet()))
                .build();

    }

    //copied from post summary
    //I didnt inject it because it causes a circular dependency
    // as postMapper already uses TagMapper as a bean
    private PostSummaryDTO PostToSummary(Post post){
        return  PostSummaryDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .draft(post.isDraft())
                .createdOn(post.getCreatedOn())
                .updatedOn(post.getUpdatedOn())
                .author(userMapper.toSummary(post.getAuthor()))
                .build();
    }

//    public Set<TagSummaryDTO> toSummaryList(List<Tag> tags){
//        return tags.stream().map(this::toSummary).collect(Collectors.toSet());
//    }

}
