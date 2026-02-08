package com.example.admin.controller;

import com.example.admin.service.ProfanityClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final ProfanityClientService profanityClientService;

    @GetMapping("/profanity")
    public ResponseEntity<Boolean> checkProfanity(@RequestParam String text) {
        // postId는 테스트용으로 임의의 값 0L 사용
        boolean isProfane = profanityClientService.checkProfanity(0L, text);
        return ResponseEntity.ok(isProfane);
    }
}
