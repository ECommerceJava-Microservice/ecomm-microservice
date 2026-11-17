package com.ecomm.authservice.auth.messaging.async.notification;

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
public class KafkaNotificationAdapter implements NotificationPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.verification-code-requested}")
    private String verificationCodeTopic;

    @Value("${app.kafka.topics.login-code-requested}")
    private String loginCodeTopic;

    @Value("${app.kafka.topics.password-reset-requested}")
    private String passwordResetTopic;

    @Override
    public void sendVerificationEmail(String email, String code) {
        publish(verificationCodeTopic, email,
                new VerificationCodeRequestedEvent(UUID.randomUUID(), email, code, Instant.now()));
    }

    @Override
    public void sendLoginOtpEmail(String email, String code) {
        publish(loginCodeTopic, email,
                new LoginCodeRequestedEvent(UUID.randomUUID(), email, code, Instant.now()));
    }

    @Override
    public void sendPasswordResetEmail(String email, String token) {
        publish(passwordResetTopic, email,
                new PasswordResetRequestedEvent(UUID.randomUUID(), email, token, Instant.now()));
    }

    private void publish(String topic, String key, Object event) {
        kafkaTemplate.send(topic, key, event).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Hadisə göndərilmədi: topic={}", topic, ex);
            } else {
                log.info("Hadisə göndərildi: topic={}, partition={}, offset={}",
                        topic,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            }
        });
    }
}