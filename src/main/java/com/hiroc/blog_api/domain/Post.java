package com.hiroc.blog_api.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(nullable = false)
    @Size(max=150)
    private String title;

    @NotNull
    @Column(nullable = false)
    @Size(max=30000)
    private String body;

    @ManyToOne
    @NotNull
    private User author;

    private String thumbnail;

    private boolean draft;

    @OneToMany(mappedBy = "post",cascade = {CascadeType.REMOVE}) //consider orphan removal also
    private Set<Comment> comments;

    @ManyToMany(cascade={CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(
            name = "POST_LIKE",
            joinColumns = @JoinColumn(name="POST_ID"),
            inverseJoinColumns = @JoinColumn(name="USER_ID")
    )
    private Set<User> likedByUsers = new HashSet<>();

//    @ManyToMany(mappedBy="posts",cascade = CascadeType.PERSIST)
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "TAG_POST",
            joinColumns = @JoinColumn(name ="POST_ID"),
            inverseJoinColumns =  @JoinColumn(name = "TAG_ID")
    )
    private Set<Tag> tags = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdOn;

    @UpdateTimestamp
    private LocalDateTime updatedOn;

    //Helper functions
    public void addTag(Tag tag){
        tags.add(tag);
        tag.getPosts().add(this);
    }





}
