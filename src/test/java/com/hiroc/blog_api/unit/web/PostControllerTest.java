package com.hiroc.blog_api.unit.web;


import com.hiroc.blog_api.controllers.PostController;
import com.hiroc.blog_api.domain.Post;
import com.hiroc.blog_api.dto.post.PostDTO;
import com.hiroc.blog_api.dto.post.PostRequestDTO;
import com.hiroc.blog_api.mappers.PostMapper;
import com.hiroc.blog_api.services.JwtService;
import com.hiroc.blog_api.services.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.hiroc.blog_api.utils.JsonUtils.toJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false) //Disable security, due to jwt filter and csrf
@WithMockUser(username="test")
public class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private PostMapper postMapper;

    @MockitoBean
    private JwtService jwtService; //Not sure why this is needed even after disabled addFilters, but it is

    private PostRequestDTO default_request;
    private MockedStatic<PostMapper> mockedPostMapper;

    @BeforeEach
    public void setUp(){
        //fill up everything
        default_request = PostRequestDTO
                .builder()
                .title("title")
                .body("body")
                .draft(true)
                .build();

        Post response = new Post();
        PostDTO default_response = new PostDTO();
        given(postService.createPost((any(PostRequestDTO.class)),any(String.class))).willReturn(response);
        given(postMapper.map(any(Post.class))).willReturn(default_response); //no longer works, changed from component to static methods
//        mockedPostMapper = mockStatic(PostMapper.class);
//        mockedPostMapper.when(()-> PostMapper.map(any(Post.class)))
//                .thenReturn(default_response);

    }

//    @AfterEach
//    void tearDown(){
//        //prevent memeory leak by closing the static mock
//        if (mockedPostMapper!=null) mockedPostMapper.close();
//    }

    @Test
    @DisplayName("TEST: No errors with valid default request - HTTP 201")
    void happyRequestOkResponse() throws Exception {
        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(default_request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("TEST: create post no title - Bad request")
    void nullPostTitleBadRequest() throws Exception {
        default_request.setTitle(null);

        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(default_request)))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("TEST: create post no body - Bad request")
    void nullPostBodyBadRequest() throws Exception {
        default_request.setBody(null);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(default_request)))
                .andExpect(status().isBadRequest());
    }






}
