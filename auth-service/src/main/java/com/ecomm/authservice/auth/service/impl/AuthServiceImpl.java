package com.ecomm.authservice.auth.service.impl;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.ecomm.authservice.auth.entity.*;
import org.springframework.util.StringUtils;
import com.ecomm.authservice.common.response.ApiResponse;
import com.ecomm.authservice.auth.dto.response.AuthResponse;
import com.ecomm.authservice.auth.dto.request.LoginRequest;
import com.ecomm.authservice.auth.dto.request.SignupRequest;
import com.ecomm.authservice.auth.exception.AccountAlreadyActiveException;
import com.ecomm.authservice.auth.exception.AccountNotActiveException;
import com.ecomm.authservice.auth.exception.EmailAlreadyExistsException;
import com.ecomm.authservice.auth.exception.EmailSendFailedException;
import com.ecomm.authservice.auth.exception.InvalidCredentialsException;
import com.ecomm.authservice.auth.exception.InvalidVerificationCodeException;
import com.ecomm.authservice.auth.exception.PasswordsDoNotMatchException;
import com.ecomm.authservice.auth.exception.RefreshTokenExpiredException;
import com.ecomm.authservice.auth.exception.RefreshTokenInvalidException;
import com.ecomm.authservice.auth.exception.ResetTokenExpiredException;
import com.ecomm.authservice.auth.exception.ResetTokenInvalidException;
import com.ecomm.authservice.auth.exception.RoleNotFoundException;
import com.ecomm.authservice.auth.exception.TooManyAttemptsException;
import com.ecomm.authservice.auth.exception.UserNotFoundException;
import com.ecomm.authservice.auth.exception.VerificationCodeAlreadySentException;
import com.ecomm.authservice.auth.exception.VerificationCodeExpiredException;
import com.ecomm.authservice.auth.exception.VerificationCodeNotFoundException;
import com.ecomm.authservice.auth.messaging.async.user.UserRegisteredEventPort;
import com.ecomm.authservice.auth.messaging.async.notification.NotificationPort;
import com.ecomm.authservice.auth.repository.RefreshTokenRepository;
import com.ecomm.authservice.auth.repository.RoleRepository;
import com.ecomm.authservice.auth.repository.UserRepository;
import com.ecomm.authservice.auth.repository.VerificationTokenRepository;
import com.ecomm.authservice.common.exception.BaseException;
import com.ecomm.authservice.security.JwtUtils;
import com.ecomm.authservice.security.UserDetailsImpl;
import com.ecomm.authservice.auth.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final NotificationPort notificationPort;
    private final UserRegisteredEventPort userRegisteredEventPort;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    @Transactional
    public ApiResponse<Void> registerUser(SignupRequest signupRequest) {
        if (!signupRequest.password().equals(signupRequest.confirmPassword())) {
            throw new PasswordsDoNotMatchException();
        }
        if (userRepository.existsByEmail(signupRequest.email())) {
            throw new EmailAlreadyExistsException();
        }

        User user = User.builder()
                .email(signupRequest.email())
                .password(encoder.encode(signupRequest.password()))
                .enabled(false)
                .build();

        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException());
        user.setRoles(Set.of(userRole));
        userRepository.save(user);

        String code = String.format("%06d", RANDOM.nextInt(1000000));
        verificationTokenRepository.save(VerificationToken.builder()
                .user(user)
                .code(code)
                .type(TokenType.REGISTER)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build());

        try {
            notificationPort.sendVerificationEmail(user.getEmail(), code);
        } catch (Exception e) {
            log.error("Email göndərmə xətası: ", e);
            throw new EmailSendFailedException();
        }

        userRegisteredEventPort.publish(
                user.getUserId(),
                user.getEmail(),
                signupRequest.firstName(),
                signupRequest.lastName()
        );

        return ApiResponse.success(null, "Qeydiyyat uğurludur! Kodu emailinizdən yoxlayın.");
    }

    @Override
    @Transactional(noRollbackFor = BaseException.class)
    public ApiResponse<Void> verifyCode(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException());
        VerificationToken token = verificationTokenRepository
                .findByUserAndType(user, TokenType.REGISTER)
                .orElseThrow(() -> new VerificationCodeNotFoundException());
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            verificationTokenRepository.delete(token);
            throw new VerificationCodeExpiredException();
        }
        checkCode(token, code);
        user.setEnabled(true);
        userRepository.save(user);
        verificationTokenRepository.delete(token);
        return ApiResponse.success(null, "Hesabınız aktivləşdirildi!");
    }

    @Override
    @Transactional
    public ApiResponse<Void> resendVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException());
        if (user.isEnabled()) {
            throw new AccountAlreadyActiveException();
        }
        verificationTokenRepository.findByUserAndType(user, TokenType.REGISTER)
                .ifPresent(token -> {
                    if (token.getExpiryDate().isAfter(LocalDateTime.now())) {
                        throw new VerificationCodeAlreadySentException();
                    }
                    verificationTokenRepository.delete(token);
                });

        String code = String.format("%06d", RANDOM.nextInt(1000000));
        verificationTokenRepository.save(VerificationToken.builder()
                .user(user).code(code).type(TokenType.REGISTER)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build());

        try {
            notificationPort.sendVerificationEmail(user.getEmail(), code);
        } catch (Exception e) {
            log.error("Email göndərmə xətası: ", e);
            throw new EmailSendFailedException();
        }
        return ApiResponse.success(null, "Yeni kod emailinizə göndərildi!");
    }

    @Override
    @Transactional
    public ApiResponse<Void> login(LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(), loginRequest.password()));
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException();
        } catch (DisabledException e) {
            throw new AccountNotActiveException();
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new UserNotFoundException());

        verificationTokenRepository.findByUserAndType(user, TokenType.LOGIN_2FA)
                .ifPresent(existingToken -> {
                    if (existingToken.getExpiryDate().isAfter(LocalDateTime.now())) {
                        throw new VerificationCodeAlreadySentException();
                    }
                    verificationTokenRepository.delete(existingToken);
                });

        String code = String.format("%06d", RANDOM.nextInt(1000000));
        verificationTokenRepository.save(VerificationToken.builder()
                .user(user).code(code).type(TokenType.LOGIN_2FA)
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .build());

        try {
            notificationPort.sendLoginOtpEmail(user.getEmail(), code);
        } catch (Exception e) {
            log.error("Email göndərmə xətası: ", e);
            throw new EmailSendFailedException();
        }

        return ApiResponse.success(null, "Doğrulama kodu emailinizə göndərildi!");
    }

    @Override
    @Transactional(noRollbackFor = BaseException.class)
    public ApiResponse<AuthResponse> verifyLoginOtp(String email, String code, HttpServletResponse response) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException());
        VerificationToken token = verificationTokenRepository
                .findByUserAndType(user, TokenType.LOGIN_2FA)
                .orElseThrow(() -> new VerificationCodeNotFoundException());
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            verificationTokenRepository.delete(token);
            throw new VerificationCodeExpiredException();
        }
        checkCode(token, code);
        verificationTokenRepository.delete(token);

        List<String> roles = user.getRoles().stream()
                .map(role -> role
                        .getRoleName()
                        .name())
                .toList();

        ResponseCookie accessCookie = jwtUtils.generateAccessCookie(user.getUserId(), user.getEmail(), roles);
        ResponseCookie refreshCookie = jwtUtils.generateRefreshCookie(user.getEmail());

        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.save(RefreshToken.builder()
                .user(user)
                .token(refreshCookie.getValue())
                .expiryDate(Instant.now().plusMillis(7 * 24 * 60 * 60 * 1000L))
                .build());
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ApiResponse.success(
                new AuthResponse(user.getUserId(), user.getEmail(), roles),
                "Uğurla daxil oldunuz!"
        );
    }

    @Override
    @Transactional
    public ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtUtils.getRefreshTokenFromCookie(request);
        if (refreshToken != null) {
            refreshTokenRepository.deleteByToken(refreshToken);
        }
        response.addHeader(HttpHeaders.SET_COOKIE, jwtUtils.clearAccessCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, jwtUtils.clearRefreshCookie().toString());
        SecurityContextHolder.clearContext();
        return ApiResponse.success(null, "Uğurla çıxış etdiniz!");
    }

    @Override
    @Transactional(noRollbackFor = BaseException.class)
    public ApiResponse<AuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtUtils.getRefreshTokenFromCookie(request);
        if (!StringUtils.hasText(refreshToken) || !jwtUtils.validateToken(refreshToken)) {
            throw new RefreshTokenInvalidException();
        }
        RefreshToken dbToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenInvalidException());
        if (dbToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(dbToken);
            throw new RefreshTokenExpiredException();
        }
        User user = dbToken.getUser();

        List<String> roles = user.getRoles().stream()
                .map(role -> role.getRoleName().name()).toList();

        ResponseCookie newAccessCookie = jwtUtils.generateAccessCookie(user.getUserId(), user.getEmail(), roles);
        response.addHeader(HttpHeaders.SET_COOKIE, newAccessCookie.toString());

        return ApiResponse.success(
                new AuthResponse(user.getUserId(), user.getEmail(), roles),
                "Token yeniləndi!"
        );
    }

    @Override
    @Transactional
    public ApiResponse<Void> forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException());
        if (!user.isEnabled()) {
            throw new AccountNotActiveException();
        }
        verificationTokenRepository.findByUserAndType(user, TokenType.RESET_PASSWORD)
                .ifPresent(verificationTokenRepository::delete);

        String token = UUID.randomUUID().toString();
        verificationTokenRepository.save(VerificationToken.builder()
                .user(user).code(token).type(TokenType.RESET_PASSWORD)
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .build());

        try {
            notificationPort.sendPasswordResetEmail(user.getEmail(), token);
        } catch (Exception e) {
            log.error("Email göndərmə xətası: ", e);
            throw new EmailSendFailedException();
        }
        return ApiResponse.success(null, "Şifrə sıfırlama linki emailinizə göndərildi!");
    }

    @Override
    @Transactional(noRollbackFor = BaseException.class)
    public ApiResponse<Void> resetPassword(String token, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new PasswordsDoNotMatchException();
        }
        VerificationToken verificationToken = verificationTokenRepository
                .findByCodeAndType(token, TokenType.RESET_PASSWORD)
                .orElseThrow(() -> new ResetTokenInvalidException());
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            verificationTokenRepository.delete(verificationToken);
            throw new ResetTokenExpiredException();
        }
        User user = verificationToken.getUser();
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
        return ApiResponse.success(null, "Şifrəniz uğurla dəyişdirildi!");
    }

    private void checkCode(VerificationToken token, String code) {
        if (token.getBlockedUntil() != null && token.getBlockedUntil().isAfter(LocalDateTime.now())) {
            throw new TooManyAttemptsException();
        }
        if (!token.getCode().equals(code)) {
            token.setAttempts(token.getAttempts() + 1);
            if (token.getAttempts() >= 3) {
                token.setBlockedUntil(LocalDateTime.now().plusMinutes(15));
            }
            verificationTokenRepository.save(token);
            throw new InvalidVerificationCodeException();
        }
    }
}