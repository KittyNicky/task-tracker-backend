package com.kittynicky.tasktrackerbackend.kafka;

import com.kittynicky.tasktrackerbackend.dto.GreetingMailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProducer {
    private final KafkaTemplate<String, GreetingMailResponse> kafkaTemplate;

    public void sendMessage(String topic, GreetingMailResponse greetingMailResponse) {
        CompletableFuture<SendResult<String, GreetingMailResponse>> future = kafkaTemplate.send(topic, greetingMailResponse);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Sent message=[{}] with offset=[{}]", greetingMailResponse, result.getRecordMetadata().offset());
            } else {
                log.error("Unable to send message=[{}] due to: {}", greetingMailResponse, ex.getMessage());
            }
        });
    }
}
