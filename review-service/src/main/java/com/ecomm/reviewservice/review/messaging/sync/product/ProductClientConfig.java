package com.ecomm.reviewservice.review.messaging.sync.product;

import com.ecomm.reviewservice.review.exception.ProductNotFoundException;
import com.ecomm.reviewservice.review.exception.ServiceUnavailableException;
import com.ecomm.reviewservice.security.TokenRelayInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ProductClientConfig {

    @Bean
    public ProductClient productClient(
            @Value("${services.product-service.url}") String baseUrl,
            TokenRelayInterceptor tokenRelayInterceptor) {

        RestClient restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestInterceptor(tokenRelayInterceptor)
                .defaultStatusHandler(status -> status.value() == 404, (request, response) -> {
                    throw new ProductNotFoundException();
                })
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new ServiceUnavailableException();
                })
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(ProductClient.class);
    }
}