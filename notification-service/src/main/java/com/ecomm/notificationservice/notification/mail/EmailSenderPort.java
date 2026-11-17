package com.ecomm.notificationservice.notification.mail;

public interface EmailSenderPort {

    void send(String to, String subject, String htmlBody);
}
