package com.swivel.ignite.reporting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Base component configurations
 */
@Configuration
public class BaseConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
