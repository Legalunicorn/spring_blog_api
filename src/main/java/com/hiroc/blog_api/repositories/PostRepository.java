package com.hiroc.blog_api.repositories;


import com.hiroc.blog_api.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {

    Optional<Post> getPostById(Integer id);

    @Query("select p from Post p " +
            "left join fetch p.author " +
            " where p.author.username=:username AND " +
            " p.draft = false")
    Set<Post> findPublishedPostsByAuthorUsername (String username);

    @Query("select p from Post p where p.draft = false order by p.createdOn desc")
    List<Post> getPostByDraftFalseOrderByCreateOnDesc();

    @Query("""
            select p from Post p 
            left join p.likes l 
            where p.draft=false
            group by p 
            order by count(l) desc
            """)
    List<Post> getPostsByDraftFalseOrderByLikeCount();

    @Query("""
            select p from Post p 
            where draft = true 
            and p.author.username=:username 
            """)
    Set<Post> findPostsByDraftTrueAndUsernameEquals(String username);


    @Query("select p from Post p " +
            "left join fetch p.tags " +
            "left join fetch p.comments " +
            "where p.id=:id")
    Optional<Post> getPostByIdWithTagsAndComments(Integer id);

}
