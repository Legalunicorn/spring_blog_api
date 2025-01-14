package com.hiroc.blog_api.dto.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
    private String message;
    private String code;
//    private Integer status;
    private Instant timestamp = Instant.now();

    public ErrorDetails(String message){
        this.message=message;
        timestamp=Instant.now();
    }


}
