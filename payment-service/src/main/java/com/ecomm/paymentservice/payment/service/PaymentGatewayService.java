package com.ecomm.paymentservice.payment.service;

import com.ecomm.paymentservice.payment.dto.response.PaymentIntentResponse;
import com.ecomm.paymentservice.payment.entity.Payment;
import com.ecomm.paymentservice.payment.entity.PaymentMethod;

public interface PaymentGatewayService {

    PaymentIntentResponse createPaymentIntent(Payment payment);

    void refund(Payment payment);

    void confirm(Payment payment, String transactionId);

    PaymentMethod getSupportedMethod();
}