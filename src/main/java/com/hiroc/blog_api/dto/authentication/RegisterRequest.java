package com.hiroc.blog_api.dto.authentication;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @Size(min=2,max=30)
    private String username;

    @NotNull
    @Size(min=2,max=50)
    private String password;
}
