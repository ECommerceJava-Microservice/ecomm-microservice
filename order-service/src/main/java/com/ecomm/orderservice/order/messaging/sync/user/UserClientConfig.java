package com.ecomm.orderservice.order.messaging.sync.user;

import com.ecomm.orderservice.order.exception.ServiceUnavailableException;
import com.ecomm.orderservice.security.TokenRelayInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class UserClientConfig {

    @Bean
    public UserClient userClient(
            @Value("${services.user-service.url}") String baseUrl,
            TokenRelayInterceptor tokenRelayInterceptor) {

        RestClient restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestInterceptor(tokenRelayInterceptor)
                .defaultStatusHandler(
                        HttpStatusCode::is5xxServerError,
                        (request, response) -> {
                            throw new ServiceUnavailableException();
                        })
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(UserClient.class);
    }
}
