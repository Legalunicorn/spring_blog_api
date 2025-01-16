package com.hiroc.blog_api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true)
    @NotNull
    private String username;

    @NotNull
    private String password; //Hashed

    private String profilePicture;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy="author")
    private Set<Comment> comments;

    @OneToMany(mappedBy="author")
    private Set<Post> createdPosts = new HashSet<>();

//    @ManyToMany(mappedBy="likedByUsers")
//    private Set<Post> likedPosts = new HashSet<>();
//

    @OneToMany(mappedBy="user",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<LikePost> likedPosts = new HashSet<>();


    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }
}
