package com.ecomm.cartservice.cart.messaging.sync.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductLookup {

    private static final String KEY_PREFIX = "product:";
    private static final Duration TTL = Duration.ofSeconds(60);

    private final ProductClient productClient;
    private final StringRedisTemplate redisTemplate;
    private final JsonMapper jsonMapper;

    public ProductInfo getProduct(UUID productId) {
        String key = KEY_PREFIX + productId;

        String cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return jsonMapper.readValue(cached, ProductInfo.class);
        }

        ProductInfo product = productClient.getProduct(productId).data();
        redisTemplate.opsForValue().set(key, jsonMapper.writeValueAsString(product), TTL);
        return product;
    }
}
