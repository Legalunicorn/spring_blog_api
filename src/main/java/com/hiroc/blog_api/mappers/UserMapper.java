package com.hiroc.blog_api.mappers;

import com.hiroc.blog_api.domain.User;
import com.hiroc.blog_api.dto.User.UserSummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserMapper {

//    private final CommentMapper commentMapper;
//    private final PostMapper postMapper;
//
//    public UserDTO map(User user){
//        return UserDTO.builder()
//                .id(user.getId())
//                .username(user.getUsername())
//
//                .build()
//    }

    public UserSummaryDTO toSummary(User user){
        return UserSummaryDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }

}
