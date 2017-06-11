package org.diveintojee.poc.vintagezerodowntime.engineserver.persistence;

import com.google.common.collect.Lists;
import org.diveintojee.poc.vintagezerodowntime.dto.Measurement;
import org.diveintojee.poc.vintagezerodowntime.engineserver.domain.MeasurementFact;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.transaction.Transactional;
import java.time.Instant;

import static org.junit.Assert.assertEquals;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RepositoryConfiguration.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional
public class MeasurementFactRepositoryTest {
    @Autowired
    private MeasurementFactRepository underTest;

    @Before
    public void before() {
        underTest.deleteAll();
    }

    @Test
    public void findFirstByBusinessIdOrderByInsertedAtDescShouldSucceed() throws Exception {
        // Given
        final Measurement measurement = Measurement.heart_rate;
        final String deviceBusinessId = "device-bid";
        final String provider = "provider";
        final int value = 121;
        Instant now = Instant.now();
        final long timestamp = now.toEpochMilli();
        final String businessId = "bid";

        MeasurementFact h0 = MeasurementFact.of(measurement, deviceBusinessId, provider, value, timestamp, businessId, now.plusMillis(5).toEpochMilli());
        MeasurementFact h1 = MeasurementFact.of(measurement, deviceBusinessId, provider, value, timestamp, businessId, now.plusMillis(3).toEpochMilli());
        underTest.save(Lists.newArrayList(h0, h1));

        // When
        final MeasurementFact actual = underTest.findFirstByBusinessIdOrderByInsertedAtDesc(businessId);

        // Then
        assertEquals(h0, actual);
    }

}
