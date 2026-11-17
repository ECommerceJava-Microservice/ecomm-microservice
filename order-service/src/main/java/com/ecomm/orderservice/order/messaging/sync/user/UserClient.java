package com.ecomm.orderservice.order.messaging.sync.user;

import com.ecomm.orderservice.common.response.ApiResponse;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange("/api/v1/addresses")
public interface UserClient {

    @GetExchange
    ApiResponse<List<AddressInfo>> getMyAddresses();
}
