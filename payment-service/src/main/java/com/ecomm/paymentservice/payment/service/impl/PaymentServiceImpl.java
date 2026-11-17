package com.ecomm.paymentservice.payment.service.impl;

import com.ecomm.paymentservice.common.response.ApiResponse;
import com.ecomm.paymentservice.common.response.PageResponse;
import com.ecomm.paymentservice.payment.messaging.sync.order.OrderClient;
import com.ecomm.paymentservice.payment.messaging.sync.order.OrderInfo;
import com.ecomm.paymentservice.payment.dto.request.PaymentRequest;
import com.ecomm.paymentservice.payment.dto.response.PaymentIntentResponse;
import com.ecomm.paymentservice.payment.dto.response.PaymentResponse;
import com.ecomm.paymentservice.payment.entity.Payment;
import com.ecomm.paymentservice.payment.entity.PaymentMethod;
import com.ecomm.paymentservice.payment.entity.PaymentStatus;
import com.ecomm.paymentservice.payment.messaging.async.payment.OrderStatusPort;
import com.ecomm.paymentservice.payment.exception.OrderNotPayableException;
import com.ecomm.paymentservice.payment.exception.PaymentAccessDeniedException;
import com.ecomm.paymentservice.payment.exception.PaymentAlreadyCompletedException;
import com.ecomm.paymentservice.payment.exception.PaymentAlreadyExistsException;
import com.ecomm.paymentservice.payment.exception.PaymentMethodNotActiveException;
import com.ecomm.paymentservice.payment.exception.PaymentNotFoundException;
import com.ecomm.paymentservice.payment.exception.PaymentNotRefundableException;
import com.ecomm.paymentservice.payment.mapper.PaymentMapper;
import com.ecomm.paymentservice.payment.repository.PaymentRepository;
import com.ecomm.paymentservice.payment.service.PaymentGatewayService;
import com.ecomm.paymentservice.payment.service.PaymentService;
import com.ecomm.paymentservice.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderClient orderClient;
    private final OrderStatusPort orderStatusPort;
    private final List<PaymentGatewayService> gatewayServices;

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ApiResponse<PaymentIntentResponse> createPaymentIntent(PaymentRequest paymentRequest) {
        PaymentGatewayService gateway = getGateway(paymentRequest.paymentMethod());
        OrderInfo order = findPayableOrder(paymentRequest.orderId());

        if (paymentRepository.existsByOrderId(order.orderId())) {
            throw new PaymentAlreadyExistsException();
        }

        Payment savedPayment = paymentRepository.save(buildPayment(order, paymentRequest.paymentMethod()));
        PaymentIntentResponse response = createIntentOrRollback(gateway, savedPayment);

        savedPayment.setGatewayTransactionId(response.paymentIntentId());
        paymentRepository.save(savedPayment);

        return ApiResponse.success(response, "Ödəniş başladıldı");
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ApiResponse<PaymentResponse> confirmPayment(String paymentIntentId) {
        Payment payment = paymentRepository.findByGatewayTransactionId(paymentIntentId)
                .orElseThrow(PaymentNotFoundException::new);

        if (payment.getPaymentStatus() == PaymentStatus.SUCCEEDED) {
            throw new PaymentAlreadyCompletedException();
        }

        getGateway(payment.getPaymentMethod()).confirm(payment, paymentIntentId);
        payment.setPaymentStatus(PaymentStatus.SUCCEEDED);
        Payment savedPayment = paymentRepository.save(payment);

        orderStatusPort.confirmOrder(payment.getOrderId());


        return ApiResponse.success(paymentMapper.toResponse(savedPayment), "Ödəniş təsdiqləndi");
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ApiResponse<PaymentResponse> refundPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(PaymentNotFoundException::new);

        if (payment.getPaymentStatus() != PaymentStatus.SUCCEEDED) {
            throw new PaymentNotRefundableException();
        }

        getGateway(payment.getPaymentMethod()).refund(payment);
        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        Payment savedPayment = paymentRepository.save(payment);

        orderStatusPort.cancelOrder(payment.getOrderId());

        return ApiResponse.success(paymentMapper.toResponse(savedPayment), "Ödəniş geri qaytarıldı");
    }

    @Override
    public ApiResponse<PaymentResponse> getPaymentByOrder(UUID orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(PaymentNotFoundException::new);

        if (!SecurityUtils.isAdmin() && !payment.getUserId().equals(SecurityUtils.getCurrentUserId())) {
            throw new PaymentAccessDeniedException();
        }

        return ApiResponse.success(paymentMapper.toResponse(payment), "Ödəniş məlumatı tapıldı");
    }

    @Override
    public ApiResponse<PageResponse<PaymentResponse>> getAllPayments(Pageable pageable) {
        Page<Payment> paymentPage = paymentRepository.findAll(pageable);

        List<PaymentResponse> content = paymentPage.getContent().stream()
                .map(paymentMapper::toResponse)
                .toList();

        PageResponse<PaymentResponse> pageResponse = new PageResponse<>(
                content,
                paymentPage.getNumber(),
                paymentPage.getSize(),
                paymentPage.getTotalElements(),
                paymentPage.getTotalPages(),
                paymentPage.isLast());

        return ApiResponse.success(pageResponse, "Bütün ödənişlər gətirildi");
    }

    // --- Monolitdən gələn köməkçi metodlar ---

    private PaymentGatewayService getGateway(PaymentMethod method) {
        return gatewayServices.stream()
                .filter(gateway -> gateway.getSupportedMethod() == method)
                .findFirst()
                .orElseThrow(() -> new PaymentMethodNotActiveException(method));
    }

    private Payment buildPayment(OrderInfo order, PaymentMethod method) {
        return Payment.builder()
                .orderId(order.orderId())
                .userId(SecurityUtils.getCurrentUserId())
                .amount(order.totalAmount())
                .paymentMethod(method)
                .build();
    }

    // --- 🔹 Mikroservisə xas köməkçi metodlar ---

    private OrderInfo findPayableOrder(UUID orderId) {
        OrderInfo order = orderClient.getOrderById(orderId).data();

        if (!order.isPending()) {
            throw new OrderNotPayableException();
        }
        return order;
    }

    private PaymentIntentResponse createIntentOrRollback(PaymentGatewayService gateway, Payment payment) {
        try {
            return gateway.createPaymentIntent(payment);
        } catch (RuntimeException e) {
            paymentRepository.delete(payment);
            throw e;
        }
    }
}