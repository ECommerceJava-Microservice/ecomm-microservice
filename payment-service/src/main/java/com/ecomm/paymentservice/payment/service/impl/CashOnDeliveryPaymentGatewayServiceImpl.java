package com.ecomm.paymentservice.payment.service.impl;

import com.ecomm.paymentservice.payment.dto.response.PaymentIntentResponse;
import com.ecomm.paymentservice.payment.entity.Payment;
import com.ecomm.paymentservice.payment.entity.PaymentMethod;
import com.ecomm.paymentservice.payment.exception.PaymentConfirmForbiddenException;
import com.ecomm.paymentservice.payment.service.PaymentGatewayService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CashOnDeliveryPaymentGatewayServiceImpl implements PaymentGatewayService {

    private static final String TRANSACTION_PREFIX = "COD-";
    private static final String ADMIN_AUTHORITY = "ROLE_ADMIN";

    @Override
    public PaymentIntentResponse createPaymentIntent(Payment payment) {
        return new PaymentIntentResponse(
                null,
                TRANSACTION_PREFIX + payment.getPaymentId(),
                payment.getOrderId());
    }

    @Override
    public void refund(Payment payment) {
        // Nağd ödənişdə xarici sistem yoxdur, pul əl ilə qaytarılır
    }

    @Override
    public void confirm(Payment payment, String transactionId) {
        if (!isCurrentUserAdmin()) {
            throw new PaymentConfirmForbiddenException();
        }
    }

    @Override
    public PaymentMethod getSupportedMethod() {
        return PaymentMethod.CASH_ON_DELIVERY;
    }

    private boolean isCurrentUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> ADMIN_AUTHORITY.equals(authority.getAuthority()));
    }
}
