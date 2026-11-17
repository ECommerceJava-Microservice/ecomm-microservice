package com.ecomm.paymentservice.payment.service;

import com.ecomm.paymentservice.common.response.ApiResponse;
import com.ecomm.paymentservice.common.response.PageResponse;
import com.ecomm.paymentservice.payment.dto.request.PaymentRequest;
import com.ecomm.paymentservice.payment.dto.response.PaymentIntentResponse;
import com.ecomm.paymentservice.payment.dto.response.PaymentResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PaymentService {

    ApiResponse<PaymentIntentResponse> createPaymentIntent(PaymentRequest paymentRequest);

    ApiResponse<PaymentResponse> confirmPayment(String paymentIntentId);

    ApiResponse<PaymentResponse> refundPayment(UUID paymentId);

    ApiResponse<PaymentResponse> getPaymentByOrder(UUID orderId);

    ApiResponse<PageResponse<PaymentResponse>> getAllPayments(Pageable pageable);
}