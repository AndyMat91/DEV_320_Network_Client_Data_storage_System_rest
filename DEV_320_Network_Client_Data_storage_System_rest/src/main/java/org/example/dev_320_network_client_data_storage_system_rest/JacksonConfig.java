package org.example.dev_320_network_client_data_storage_system_rest;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

//Конфигурация Jackson, чтобы он игнорировал все прокси-классы, установкой свойства FAIL_ON_EMPTY_BEANS в false.
//Не работали методы EntityManager, это решило проблему
@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        return new Jackson2ObjectMapperBuilder()
                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
}