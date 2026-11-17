package com.ecomm.userservice.user.messaging.async.auth;

import com.ecomm.userservice.user.dto.request.CreateUserProfileRequest;
import com.ecomm.userservice.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventConsumer {

    private final UserProfileService userProfileService;

    @KafkaListener(
            topics = "${app.kafka.topics.user-registered}",
            properties = "spring.json.value.default.type=com.ecomm.userservice.user.messaging.async.auth.UserRegisteredEvent")
    public void onUserRegistered(UserRegisteredEvent event) {
        log.info("Qeydiyyat hadisəsi qəbul edildi: eventId={}, userId={}", event.eventId(), event.userId());
        userProfileService.createProfile(
                new CreateUserProfileRequest(event.userId(), event.firstName(), event.lastName()));
    }
}
