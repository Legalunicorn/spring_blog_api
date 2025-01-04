package com.hiroc.blog_api.repositories;


import com.hiroc.blog_api.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {

    Optional<Post> getPostById(Integer id);


    @Query("select p from Post p " +
            "left join fetch p.tags " +
            "left join fetch p.comments " +
            "where p.id=:id")
    Optional<Post> getPostByIdWithTagsAndComments(Integer id);

}
