package com.example.kv;

import io.tarantool.client.factory.TarantoolCrudClientBuilder;
import io.tarantool.client.factory.TarantoolFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public TarantoolCrudClientBuilder clientSettings() {
        return TarantoolFactory.crud()
                .withHost("localhost")
                .withPort(3301)
                .withUser("storage")
                .withPassword("secret-cluster-cookie");
    }
}