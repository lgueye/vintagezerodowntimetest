package org.diveintojee.poc.vintagezerodowntime.e2e.steps;

import org.assertj.core.util.Lists;
import org.diveintojee.poc.vintagezerodowntime.dto.Measurement;
import org.diveintojee.poc.vintagezerodowntime.dto.MeasurementFactDTO;
import org.diveintojee.poc.vintagezerodowntime.engineclient.EngineClient;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.junit.Assert.assertEquals;

@Component
public class EngineSteps {

	private static final Logger LOG = LoggerFactory.getLogger(EngineSteps.class);

	@Autowired
	private EngineClient engineClient;

	private List<MeasurementFactDTO> results = Lists.newArrayList();

	@When("provider $provider sends the following facts: $rows")
	public void sendFacts(String provider, ExamplesTable rows) {
		final List<MeasurementFactDTO> dtos = rows.getRowsAs(MeasurementFactDTO.class);
		dtos.forEach(dto -> {
			LOG.debug("Sending fact {} on provider {} endpoint", dto, provider);
			final MeasurementFactDTO created = engineClient.createMeasurementFact(provider, dto);
			LOG.debug("Received the created fact {} on provider {} endpoint", created);
		});
	}

	@When("consumer searchs for facts with the following criteria: $rows")
	public void findFactsByCriteria(ExamplesTable rows) {
		final List<MeasurementFactDTO> criteria = rows.getRowsAs(MeasurementFactDTO.class);
		final MeasurementFactDTO firstCriterion = criteria.iterator().next();
		final Measurement measurement = firstCriterion.getMeasurement();
		final String deviceBusinessId = firstCriterion.getDeviceBusinessId();
		final String provider = firstCriterion.getProvider();
		results = engineClient.findMeasurementFactsByCriteria(MeasurementFactDTO.of(measurement, deviceBusinessId, provider));
	}

	@Then("the following facts were persisted: $rows")
	public void verifyFactsWerePersisted(ExamplesTable rows) {
		final List<MeasurementFactDTO> expected = rows.getRowsAs(MeasurementFactDTO.class);
		LOG.debug("Comparing expected {} and actual {}", expected, results);
		assertEquals(expected, results);
	}

}
