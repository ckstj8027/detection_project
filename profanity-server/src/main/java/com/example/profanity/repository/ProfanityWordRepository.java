package com.example.profanity.repository;

import com.example.profanity.domain.ProfanityWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProfanityWordRepository extends JpaRepository<ProfanityWord, Long> {
    
    @Query("SELECT p.word FROM ProfanityWord p")
    List<String> findAllWords();
}
