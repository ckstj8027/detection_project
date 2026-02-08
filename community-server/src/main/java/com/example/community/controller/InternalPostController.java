package com.example.community.controller;

import com.example.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/posts")
@RequiredArgsConstructor
public class InternalPostController {

    private final PostService postService;

    @PutMapping("/{postId}/status")
    public ResponseEntity<Void> updatePostStatus(@PathVariable Long postId, @RequestParam boolean isVisible) {
        postService.updatePostStatus(postId, isVisible);
        return ResponseEntity.ok().build();
    }
}
