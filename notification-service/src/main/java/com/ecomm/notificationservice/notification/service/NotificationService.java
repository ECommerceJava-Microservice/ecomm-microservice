package com.ecomm.notificationservice.notification.service;

public interface NotificationService {

    void sendVerificationEmail(String email, String code);
    void sendLoginOtpEmail(String email, String code);
    void sendPasswordResetEmail(String email, String token);
}
