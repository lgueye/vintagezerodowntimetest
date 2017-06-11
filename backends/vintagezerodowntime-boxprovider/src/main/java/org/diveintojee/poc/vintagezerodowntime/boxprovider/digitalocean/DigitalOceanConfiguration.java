package org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean;

import org.assertj.core.util.Strings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.myjeeva.digitalocean.impl.DigitalOceanClient;

/**
 * Created by <a href="mailto:louis.gueye@domo-safety.com">Louis Gueye</a>.
 */
@Configuration
public class DigitalOceanConfiguration {
    @Bean
    public DigitalOceanClient digitalOceanClient() {
        final String authToken = System.getenv("DO_API_TOKEN");
        if (Strings.isNullOrEmpty(authToken)) {
            throw new IllegalStateException("Please set your [DO_API_TOKEN] env value");
        }
        return new DigitalOceanClient("v2", authToken);
    }

}
