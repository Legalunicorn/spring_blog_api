package com.hiroc.blog_api.controllers;


import com.hiroc.blog_api.domain.Tag;
import com.hiroc.blog_api.dto.tag.TagDTO;
import com.hiroc.blog_api.dto.tag.TagSummaryDTO;
import com.hiroc.blog_api.mappers.TagMapper;
import com.hiroc.blog_api.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping("/{name}/posts")
    public ResponseEntity<TagDTO> getPostsOfTag(@PathVariable String name){
        Tag tag = tagService.findPostsByTag(name);
        TagDTO tag_dto =  tagMapper.map(tag);
        return ResponseEntity.ok(tag_dto);
    }

    @GetMapping
    public ResponseEntity<Set<TagSummaryDTO>> getTags(){
        Set<Tag> tags = tagService.getTags();
        Set<TagSummaryDTO> tags_dtos = tags.stream().map(tagMapper::toSummary).collect(Collectors.toSet());
        return ResponseEntity.ok(tags_dtos);
    }

}
