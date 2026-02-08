package com.example.admin.listener;

import com.example.admin.config.RabbitMqConfig;
import com.example.admin.dto.PostCreatedEvent;
import com.example.admin.service.ProfanityClientService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostEventListener {

    private final ProfanityClientService profanityClientService;
    private final RestTemplate restTemplate = new RestTemplate(); // Simple RestTemplate for demo

    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    public void handlePostCreatedEvent(PostCreatedEvent event, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        log.info("Received PostCreatedEvent: {}", event);

        try {
            boolean isProfane = profanityClientService.checkProfanity(event.getPostId(), event.getContent());
            
            if (isProfane) {
                log.info("Post {} contains profanity. Keeping it hidden.", event.getPostId());
                // Optionally call community server to mark as deleted or flagged
            } else {
                log.info("Post {} is clean. Making it visible.", event.getPostId());
                // Call Community Server API to make the post visible
                String url = "http://localhost:8080/internal/posts/" + event.getPostId() + "/status?isVisible=true";
                restTemplate.put(url, null);
            }

            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("Error processing event for post {}: {}", event.getPostId(), e.getMessage());
            // Nack and requeue in case of error (e.g., gRPC service down)
            channel.basicNack(tag, false, true);
        }
    }
}
