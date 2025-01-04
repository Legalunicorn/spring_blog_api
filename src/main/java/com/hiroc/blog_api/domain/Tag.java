package com.hiroc.blog_api.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
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

    @ManyToMany(mappedBy="tags",cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Post> posts = new HashSet<>();



}
