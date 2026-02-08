package com.example.profanity.service;

import com.example.profanity.repository.ProfanityWordRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ahocorasick.trie.Trie;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfanityCheckService {

    private final ProfanityWordRepository profanityWordRepository;
    private Trie trie;

    @PostConstruct
    public void init() {
        log.info("Loading profanity words into memory...");
        List<String> words = profanityWordRepository.findAllWords();
        
        Trie.TrieBuilder builder = Trie.builder()
                .ignoreCase()
                .onlyWholeWords(); // 부분 일치도 잡고 싶으면 이 줄을 제거하세요.

        for (String word : words) {
            builder.addKeyword(word);
        }

        this.trie = builder.build();
        log.info("Loaded {} profanity words into Aho-Corasick Trie.", words.size());
    }

    public boolean isProfane(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        return trie.containsMatch(text);
    }
    
    public void reloadWords() {
        init();
    }
}
