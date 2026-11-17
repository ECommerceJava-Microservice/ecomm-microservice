package com.ecomm.paymentservice.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenRelayInterceptor implements ClientHttpRequestInterceptor {

    private final JwtUtils jwtUtils;

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution
    ) throws IOException {

        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            String token = resolveToken(attributes.getRequest());
            if (StringUtils.hasText(token)) {
                request.getHeaders().setBearerAuth(token);
            }
        }

        return execution.execute(request, body);
    }

    private String resolveToken(HttpServletRequest incomingRequest) {
        String authorizationHeader = incomingRequest.getHeader("Authorization");

        if (StringUtils.hasText(authorizationHeader)
                && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        return jwtUtils.getAccessTokenFromCookie(incomingRequest);
    }
}
