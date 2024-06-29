package org.example.dev_310_1_network_client_data_storage_system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeanAppConfig {

    @Bean
    public WebClient webClient(@Value("${rest.baseurl}") String baseUrl){
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

}