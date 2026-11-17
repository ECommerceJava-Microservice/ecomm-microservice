package com.ecomm.notificationservice.notification.messaging.async.auth;

import com.ecomm.notificationservice.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "${app.kafka.topics.verification-code-requested}",
            properties = "spring.json.value.default.type=com.ecomm.notificationservice.notification.messaging.async.auth.VerificationCodeRequestedEvent"
    )
    public void onVerificationCodeRequested(VerificationCodeRequestedEvent event) {
        notificationService.sendVerificationEmail(event.email(), event.code());
    }

    @KafkaListener(
            topics = "${app.kafka.topics.login-code-requested}",
            properties = "spring.json.value.default.type=com.ecomm.notificationservice.notification.messaging.async.auth.LoginCodeRequestedEvent"
    )
    public void onLoginCodeRequested(LoginCodeRequestedEvent event) {
        notificationService.sendLoginOtpEmail(event.email(), event.code());
    }

    @KafkaListener(
            topics = "${app.kafka.topics.password-reset-requested}",
            properties = "spring.json.value.default.type=com.ecomm.notificationservice.notification.messaging.async.auth.PasswordResetRequestedEvent"
    )
    public void onPasswordResetRequested(PasswordResetRequestedEvent event) {
        notificationService.sendPasswordResetEmail(event.email(), event.token());
    }
}
