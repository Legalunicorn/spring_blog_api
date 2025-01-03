package com.hiroc.blog_api.repositories;


import com.hiroc.blog_api.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {

    Optional<Post> getPostById(Integer id);
}
