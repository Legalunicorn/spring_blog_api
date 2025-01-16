package com.hiroc.blog_api.services;


import com.hiroc.blog_api.domain.User;
import com.hiroc.blog_api.dto.User.UserRequestDTO;
import com.hiroc.blog_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User updateProfile(UserRequestDTO userRequestDTO,User user){
        //The user is authenticated, so the controller can just provide the user object
        if (!userRequestDTO.getProfilePicture().isEmpty()){
            user.setProfilePicture(userRequestDTO.getProfilePicture());
            userRepository.save(user);
        }
        return user;


    }
}
