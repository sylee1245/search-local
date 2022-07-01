package com.test.searchlocal.infrastructure.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@ConstructorBinding
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Getter
@AllArgsConstructor
@Validated
public class ApplicationProperties {

    private final Client client;

    @Value
    public static class Client {
        Kakao kakao;
        Naver naver;

        @Value
        @Getter
        public static class Kakao {
            String token;
            String url;
        }

        @Value
        @Getter
        public static class Naver {
            String clientId;
            String clientSecret;
            String url;
        }
    }
}
