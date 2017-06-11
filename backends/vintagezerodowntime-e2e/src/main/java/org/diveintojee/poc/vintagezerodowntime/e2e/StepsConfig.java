package org.diveintojee.poc.vintagezerodowntime.e2e;

import org.diveintojee.poc.vintagezerodowntime.engineclient.EngineClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.diveintojee.poc.vintagezerodowntime.e2e.steps"})
public class StepsConfig {
    @Bean
    public EngineClient engineClient() {
        return new EngineClient();
    }

}
