package com.example.community.service;

import com.example.community.config.RabbitMqConfig;
import com.example.community.domain.Post;
import com.example.community.dto.PostCreatedEvent;
import com.example.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public Post createPost(String title, String content, String author) {
        Post post = new Post(title, content, author);
        post = postRepository.save(post);

        // Send event to RabbitMQ
        PostCreatedEvent event = new PostCreatedEvent(post.getId(), post.getContent());
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY, event);

        return post;
    }

    @Transactional
    public void updatePostStatus(Long postId, boolean isVisible) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + postId));
        post.setVisible(isVisible);
    }

    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}
