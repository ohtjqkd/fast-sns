package com.fastcampus.fastsns.controller;

import com.fastcampus.fastsns.controller.request.PostCreateRequest;
import com.fastcampus.fastsns.controller.request.PostModifyRequest;
import com.fastcampus.fastsns.controller.response.PostResponse;
import com.fastcampus.fastsns.controller.response.Response;
import com.fastcampus.fastsns.model.Post;
import com.fastcampus.fastsns.model.entity.UserEntity;
import com.fastcampus.fastsns.repository.UserEntityRepository;
import com.fastcampus.fastsns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication) {

        postService.create(request.getTitle(), request.getBody(), authentication.getName());
        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable Integer postId, @RequestBody PostModifyRequest request, Authentication authentication) {
        Post post = postService.modify(request.getTitle(), request.getBody(), authentication.getName(), postId);
        return Response.success(PostResponse.fromPost(post));
    }
}
