package com.hiroc.blog_api.exceptions;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String message) {
        super(message);
    }

    public PostNotFoundException(Integer id){
      super("Post of id "+id+" was not found.");
    }
}
