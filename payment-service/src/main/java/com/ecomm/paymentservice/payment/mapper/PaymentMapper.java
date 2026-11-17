package com.ecomm.paymentservice.payment.mapper;

import com.ecomm.paymentservice.payment.dto.response.PaymentResponse;
import com.ecomm.paymentservice.payment.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentResponse toResponse(Payment payment);
}