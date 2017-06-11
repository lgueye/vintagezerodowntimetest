package org.diveintojee.poc.vintagezerodowntime.boxprovider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean.DigitalOceanProviderCreateLineCommand;
import org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean.DigitalOceanProviderDropLineCommand;
import org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean.domain.Intent;
import org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean.domain.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class VintageZeroDowntimeBoxProvider implements CommandLineRunner {

//	private static final Logger LOGGER = LoggerFactory.getLogger(BlueGreenUpgradeBoxProvider.class);

	@Value("${request}")
	String requestAsString;
	private ObjectMapper objectMapper;
	private DigitalOceanProviderCreateLineCommand digitalOceanProviderCreateLineCommand;
	private DigitalOceanProviderDropLineCommand digitalOceanProviderDropLineCommand;

	void setRequestAsString(String requestAsString) {
		this.requestAsString = requestAsString;
	}

	@Autowired
	void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Autowired
	public void setDigitalOceanProviderCreateLineCommand(DigitalOceanProviderCreateLineCommand digitalOceanProviderCreateLineCommand) {
		this.digitalOceanProviderCreateLineCommand = digitalOceanProviderCreateLineCommand;
	}

	@Autowired
	public void setDigitalOceanProviderDropLineCommand(DigitalOceanProviderDropLineCommand digitalOceanProviderDropLineCommand) {
		this.digitalOceanProviderDropLineCommand = digitalOceanProviderDropLineCommand;
	}

	@Override
	public void run(String... args) throws IOException, RequestUnsuccessfulException, DigitalOceanException {
		Request request = objectMapper.readValue(requestAsString, Request.class);
		final SupportedProvider provider = request.getProvider();
		switch (provider) {
			case digitalocean:
				final Intent intent = request.getIntent();
				switch (intent) {
					case create:
						digitalOceanProviderCreateLineCommand.setRequest(request);
						digitalOceanProviderCreateLineCommand.run();
						break;
					case drop:
						digitalOceanProviderDropLineCommand.run();
						break;
					default:
						throw new UnsupportedOperationException("Supported operations are " + Lists.newArrayList(Intent.values()) + ", got " + intent);
				}
				break;
			default:
				throw new UnsupportedOperationException("Supported providers are " + Lists.newArrayList(SupportedProvider.values()) + ", got " + provider);
		}
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(VintageZeroDowntimeBoxProvider.class, args);
	}

}
