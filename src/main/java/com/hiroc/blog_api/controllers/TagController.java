package com.hiroc.blog_api.controllers;


import com.hiroc.blog_api.mappers.TagMapper;
import com.hiroc.blog_api.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

//    @GetMapping("/{name}/posts")
//    public ResponseEntity<TagDTO> getPostsOfTag(@PathVariable String name){
//        Tag tag = tagService.findPostsByTag(name);
//        return tagMapper.to
//
//    }

}
