package org.diveintojee.poc.vintagezerodowntime.dbupgrader;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;

import java.io.IOException;
import java.util.List;

@SpringBootApplication(exclude = {JpaRepositoriesAutoConfiguration.class})
public class DbUpgrader implements CommandLineRunner {

	@Autowired
	private Flyway flyway;

	@Value("${datasource.url}")
	protected String url;

	@Value("${flyway.clean}")
	private boolean clean;

	@Value("${flyway.migrate}")
	private boolean migrate = true;

	void setFlyway(Flyway flyway) {
		this.flyway = flyway;
	}

	void setUrl(String url) {
		this.url = url;
	}

	void setClean(boolean clean) {
		this.clean = clean;
	}

	void setMigrate(boolean migrate) {
		this.migrate = migrate;
	}

	@Override
	public void run(String... args) throws IOException {
		if (Strings.isNullOrEmpty(url)) {
			throw new IllegalArgumentException("Please provide a datasource url with pattern [jdbc:<db>:<driver-specific-url-format>]");
		}
		final Iterable<String> tokens = Splitter.on(":").split(url);
		final List<String> tokensAsList = Lists.newArrayList(tokens);
		final int tokenListSize = tokensAsList.size();
		if (tokenListSize < 3) {
			throw new IllegalArgumentException("Expected datasource url pattern [jdbc:<db>:<driver-specific-url-format>] (at least 3 tokens , got " + tokenListSize + ")");
		}
		// target db is 2nd token
		String targetDb = tokensAsList.get(1);
		try {
			SupportedDb.valueOf(targetDb);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Supported databases are " + Lists.newArrayList(SupportedDb.values()) + ", got " + targetDb);
		}
		flyway.setLocations("db." + targetDb + ".migrations");

		if (clean) {
			this.flyway.clean();
		}
		if (migrate) {
			this.flyway.migrate();
		}
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(DbUpgrader.class, args);
	}

}
