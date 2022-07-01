package com.test.searchlocal;

import com.test.searchlocal.infrastructure.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class SearchLocalApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchLocalApplication.class, args);
    }

}
