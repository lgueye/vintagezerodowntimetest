package org.diveintojee.poc.vintagezerodowntime.boxprovider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@SuppressWarnings("unchecked")
public class VintageZeroDowntimeBoxProviderConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
