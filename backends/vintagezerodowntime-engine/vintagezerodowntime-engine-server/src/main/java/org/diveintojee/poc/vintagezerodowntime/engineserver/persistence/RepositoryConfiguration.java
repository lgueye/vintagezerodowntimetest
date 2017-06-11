package org.diveintojee.poc.vintagezerodowntime.engineserver.persistence;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"org.diveintojee.poc.vintagezerodowntime.engineserver"})
@EntityScan(basePackages = {"org.diveintojee.poc.vintagezerodowntime.engineserver.domain"})
@EnableJpaRepositories(basePackages = {"org.diveintojee.poc.vintagezerodowntime.engineserver.persistence"})
@EnableJpaAuditing
@EnableTransactionManagement
public class RepositoryConfiguration {
}
