package com.ecomm.notificationservice.notification.service.impl;

import com.ecomm.notificationservice.notification.mail.EmailSenderPort;
import com.ecomm.notificationservice.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final EmailSenderPort emailSender;

    @Value("${app.frontend.reset-password-url}")
    private String resetPasswordUrl;

    @Override
    public void sendVerificationEmail(String email, String code) {
        String html = "<h1>Xoş gəlmisiniz!</h1>"
                + "<p>Qeydiyyatınızı tamamlamaq üçün təsdiqləmə kodunuz:</p>"
                + "<h2 style='color: #2e6c80;'>" + code + "</h2>"
                + "<p>Bu kod 15 dəqiqə ərzində keçərlidir.</p>";

        emailSender.send(email, "E-Commerce | Hesab Təsdiqləmə Kodu", html);
        log.info("Təsdiq kodu maili göndərildi");
    }

    @Override
    public void sendLoginOtpEmail(String email, String code) {
        String html = "<h1>Giriş Doğrulaması</h1>"
                + "<p>Hesabınıza giriş üçün doğrulama kodunuz:</p>"
                + "<h2 style='color: #2e6c80;'>" + code + "</h2>"
                + "<p>Bu kod 5 dəqiqə ərzində keçərlidir.</p>";

        emailSender.send(email, "E-Commerce | Giriş Doğrulama Kodu", html);
        log.info("Login kodu maili göndərildi");
    }

    @Override
    public void sendPasswordResetEmail(String email, String token) {
        String link = resetPasswordUrl + "?token=" + token;

        String html = "<h1>Şifrə Sıfırlama</h1>"
                + "<p>Şifrənizi sıfırlamaq üçün aşağıdakı linkə klikləyin:</p>"
                + "<p><a href='" + link + "'>Şifrəmi sıfırla</a></p>"
                + "<p>Bu link 30 dəqiqə ərzində keçərlidir. Əgər bu sorğunu siz etməmisinizsə, "
                + "bu məktubu nəzərə almayın.</p>";

        emailSender.send(email, "E-Commerce | Şifrə Sıfırlama", html);
        log.info("Şifrə sıfırlama maili göndərildi");
    }

}