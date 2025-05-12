package com.three.recipingeventservicebe.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient userClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://user-service") // baseUrl()은 서비스 discovery 방식(Eureka, K8s DNS 등)에 따라 달라질 수 있음
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}

