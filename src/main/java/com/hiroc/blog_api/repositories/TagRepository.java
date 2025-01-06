package com.hiroc.blog_api.repositories;

import com.hiroc.blog_api.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag,Integer> {

    Optional<Tag> findByName(String name);

    Set<Tag> findAllByNameIn(List<String> names);

    @Query("""
            select t from Tag t
            left join fetch t.posts
            where t.name=:tag_name
            """)
    Tag findTagWithPosts(String tag_name);
}
