package com.ecomm.authservice.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    PASSWORDS_DO_NOT_MATCH(HttpStatus.BAD_REQUEST, "Daxil edilən şifrələr bir-biri ilə üst-üstə düşmür"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "Bu email artıq sistemdə var!"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "İstifadəçi tapılmadı"),
    ROLE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "Rol tapılmadı"),
    EMAIL_SEND_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "Email göndərilməsi uğursuz oldu. Zəhmət olmasa yenidən cəhd edin."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Email və ya parol yanlışdır"),
    ACCOUNT_NOT_ACTIVE(HttpStatus.FORBIDDEN, "Hesabınız aktiv deyil!"),
    ACCOUNT_ALREADY_ACTIVE(HttpStatus.BAD_REQUEST, "Hesabınız artıq aktivdir!"),
    VERIFICATION_CODE_NOT_FOUND(HttpStatus.BAD_REQUEST, "Kod tapılmadı!"),
    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "Kodun vaxtı bitib!"),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "Kod yanlışdır!"),
    TOO_MANY_ATTEMPTS(HttpStatus.TOO_MANY_REQUESTS, "Çox sayda yanlış cəhd. 15 dəqiqə sonra yenidən cəhd edin."),
    VERIFICATION_CODE_ALREADY_SENT(HttpStatus.TOO_MANY_REQUESTS, "Doğrulama kodu artıq göndərilib. Kodun vaxtının bitməsini gözləyin."),
    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "Refresh Token etibarsızdır. Yenidən daxil olun."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Seansın vaxtı bitib. Yenidən daxil olun."),
    RESET_TOKEN_INVALID(HttpStatus.BAD_REQUEST, "Token etibarsızdır!"),
    RESET_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "Tokenin vaxtı bitib. Yenidən cəhd edin.");

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }
}
