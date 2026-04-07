package com.example.kv.config;

import io.tarantool.client.TarantoolClient;
import io.tarantool.client.factory.TarantoolBoxClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TarantoolConfig {
    @Bean
    public TarantoolClient tarantoolClient() throws Exception {
        return new TarantoolBoxClientBuilder()
                .withHost("localhost")
                .withPort(3301)
                .withUser("guest")
                .withPassword(null)
                .build();
    }
}