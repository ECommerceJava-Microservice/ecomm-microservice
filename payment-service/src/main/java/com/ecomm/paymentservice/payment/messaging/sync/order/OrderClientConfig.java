package com.ecomm.paymentservice.payment.messaging.sync.order;

import com.ecomm.paymentservice.payment.exception.OrderAccessDeniedException;
import com.ecomm.paymentservice.payment.exception.OrderNotFoundException;
import com.ecomm.paymentservice.payment.exception.ServiceUnavailableException;
import com.ecomm.paymentservice.security.TokenRelayInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class OrderClientConfig {

    @Bean
    public OrderClient orderClient(
            @Value("${services.order-service.url}") String baseUrl,
            TokenRelayInterceptor tokenRelayInterceptor) {

        RestClient restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestInterceptor(tokenRelayInterceptor)
                .defaultStatusHandler(status -> status.value() == 404, (request, response) -> {
                    throw new OrderNotFoundException();
                })
                .defaultStatusHandler(status -> status.value() == 403, (request, response) -> {
                    throw new OrderAccessDeniedException();
                })
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new ServiceUnavailableException();
                })
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(OrderClient.class);
    }
}
