package com.ecomm.orderservice.order.dto.request;

import com.ecomm.orderservice.common.constants.AppConstants;
import com.ecomm.orderservice.common.request.PageFilterRequest;
import com.ecomm.orderservice.order.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderFilterRequest extends PageFilterRequest {

    private OrderStatus status;

    public OrderFilterRequest() {
        setSortBy(AppConstants.SORT_ORDERS_BY);
    }
}
