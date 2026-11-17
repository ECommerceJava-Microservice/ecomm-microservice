package com.ecomm.authservice.auth.messaging.async.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaUserEventAdapter implements UserRegisteredEventPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.user-registered}")
    private String userRegisteredTopic;

    @Override
    public void publish(UUID userId, String email, String firstName, String lastName) {
        UserRegisteredEvent event = new UserRegisteredEvent(
                UUID.randomUUID(), userId, email, firstName, lastName, Instant.now());

        kafkaTemplate.send(userRegisteredTopic, userId.toString(), event).whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("Hadisə göndərilmədi: topic={}, userId={}", userRegisteredTopic, userId, exception);
                return;
            }
            log.info("Hadisə göndərildi: topic={}, userId={}, partition={}, offset={}",
                    userRegisteredTopic, userId,
                    result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
        });
    }
}
