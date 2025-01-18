package com.hiroc.blog_api.services;


import com.hiroc.blog_api.domain.Post;
import com.hiroc.blog_api.domain.User;
import com.hiroc.blog_api.dto.User.UserRequestDTO;
import com.hiroc.blog_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostService postService;

    public User getUser(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("user not found: "+username));

        Set<Post> posts =postService.findPublishesPostsByAuthorUsername(username);
//        UserProfileDTO userProfileDTO = UserProfileDTO
//                .builder()
//                .postSummaryDTOS(posts)
//                .build();

        return user;
    }

    public User updateProfile(UserRequestDTO userRequestDTO,User user){
        //The user is authenticated, so the controller can just provide the user object
        if (!userRequestDTO.getProfilePicture().isEmpty()){
            user.setProfilePicture(userRequestDTO.getProfilePicture());
            userRepository.save(user);
        }
        return user;


    }
}
