package org.diveintojee.poc.vintagezerodowntime.dbupgrader;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.sql.Driver;

@Configuration
@SuppressWarnings("unchecked")
public class DbUpgraderConfiguration {

	@Value("${datasource.driverClassName}")
	protected String driverClassName;

	@Value("${datasource.username}")
	protected String userName;

	@Value("${datasource.password:}")
	protected String password;

	@Value("${datasource.url}")
	protected String url;

	private SimpleDriverDataSource dataSource(String driverClassName, String userName, String url, String password) throws ClassNotFoundException {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass((Class<? extends Driver>) Class.forName(driverClassName));
		dataSource.setUsername(userName);
		dataSource.setUrl(url);
		dataSource.setPassword(password);
		return dataSource;
	}

	@Bean(name = "jdbcTemplate")
	JdbcTemplate jdbcTemplate() throws ClassNotFoundException {
		return new JdbcTemplate(dataSource(driverClassName, userName, url, password));
	}

	@Bean
	public Flyway flyway() throws ClassNotFoundException {
		Flyway flyway = new Flyway();
		flyway.setDataSource(jdbcTemplate().getDataSource());
		flyway.setBaselineOnMigrate(true);
		return flyway;
	}

}
