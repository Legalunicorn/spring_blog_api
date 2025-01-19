package com.hiroc.blog_api.services;


import com.hiroc.blog_api.domain.Tag;
import com.hiroc.blog_api.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {

    private final TagRepository tagRepository;

    public Tag findPostsByTag(String tag_name){
        Tag tag = tagRepository.findTagWithPosts(tag_name);
        log.debug("tag null: {}",tag==null);
        return tag;
    }

    public Set<Tag> getTags() {
        return tagRepository.findTagsOrderByPostCount();
    }
}
