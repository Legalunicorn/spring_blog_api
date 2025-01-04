package com.hiroc.blog_api.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(unique = true)
    @NotNull
    private String name;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "TAG_POST",
            joinColumns = @JoinColumn(name ="TAG_ID"),
            inverseJoinColumns =  @JoinColumn(name = "POST_ID")
    )
    private Set<Post> posts = new HashSet<>();



}
