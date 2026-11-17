package com.ecomm.reviewservice.review.messaging.sync.user;

import com.ecomm.reviewservice.common.response.ApiResponse;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/api/v1/users")
public interface UserClient {

    @GetExchange("/me")
    ApiResponse<UserInfo> getCurrentUser();
}
