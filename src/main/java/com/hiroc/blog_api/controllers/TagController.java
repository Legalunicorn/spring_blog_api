package com.hiroc.blog_api.controllers;


import com.hiroc.blog_api.domain.Tag;
import com.hiroc.blog_api.dto.tag.TagDTO;
import com.hiroc.blog_api.mappers.TagMapper;
import com.hiroc.blog_api.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
