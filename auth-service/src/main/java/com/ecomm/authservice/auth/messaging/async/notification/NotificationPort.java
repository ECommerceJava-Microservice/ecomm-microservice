package com.ecomm.authservice.auth.messaging.async.notification;

public interface NotificationPort {
    void sendVerificationEmail(String email, String code);
    void sendLoginOtpEmail(String email, String code);
    void sendPasswordResetEmail(String email, String token);
}