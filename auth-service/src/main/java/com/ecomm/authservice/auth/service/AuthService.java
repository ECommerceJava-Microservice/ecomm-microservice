package com.ecomm.authservice.auth.service;

import com.ecomm.authservice.common.response.ApiResponse;
import com.ecomm.authservice.auth.dto.response.AuthResponse;
import com.ecomm.authservice.auth.dto.request.LoginRequest;
import com.ecomm.authservice.auth.dto.request.SignupRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    ApiResponse<Void> registerUser(SignupRequest signupRequest);
    ApiResponse<Void> verifyCode(String email, String code);
    ApiResponse<Void> resendVerificationCode(String email);
    ApiResponse<Void> login(LoginRequest loginRequest, HttpServletResponse response);
    ApiResponse<AuthResponse> verifyLoginOtp(String email, String code, HttpServletResponse response);
    ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse response);
    ApiResponse<AuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response);
    ApiResponse<Void> forgotPassword(String email);
    ApiResponse<Void> resetPassword(String token, String newPassword, String confirmPassword);
}
