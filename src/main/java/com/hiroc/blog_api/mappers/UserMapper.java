package com.hiroc.blog_api.mappers;

import com.hiroc.blog_api.domain.User;
import com.hiroc.blog_api.dto.User.UserSummaryDTO;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    public UserSummaryDTO toSummary(User user){
        return UserSummaryDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }

}
