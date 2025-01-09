package com.hiroc.blog_api.repositories;

import com.hiroc.blog_api.domain.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikePostRepository extends JpaRepository<LikePost,Integer> {

    boolean existsByPostIdAndUserId(Integer postId, Integer userId);

    void deleteByPostIdAndUserId(Integer postId, Integer userId);

    
}
