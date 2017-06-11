package org.diveintojee.poc.vintagezerodowntime.engineserver.converters;

import org.diveintojee.poc.vintagezerodowntime.dto.MeasurementFactDTO;
import org.diveintojee.poc.vintagezerodowntime.engineserver.domain.MeasurementFact;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.assertEquals;

/**
 * @author louis.gueye@gmail.com
 */
public class MeasurementFactToMeasurementFactDTOConverterTest {

    private MeasurementFactToMeasurementFactDTOConverter underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new MeasurementFactToMeasurementFactDTOConverter();
    }

    @Test
    public void convertShouldSucceed() throws Exception {
        // Given
        final String businessId = "bid";
        final String deviceBusinessId = "device-bid";
        final String provider = "provider";
        final int value = 121;
        final long timestamp = Instant.now().toEpochMilli();
        MeasurementFact source = new MeasurementFact();
        source.setBusinessId(businessId);
        source.setDeviceBusinessId(deviceBusinessId);
        source.setProvider(provider);
        source.setValue(value);
        source.setTimestamp(timestamp);

        // When
        final MeasurementFactDTO actual = underTest.convert(source);

        // Then
        assertEquals(businessId, actual.getBusinessId());
        assertEquals(deviceBusinessId, actual.getDeviceBusinessId());
        assertEquals(provider, actual.getProvider());
        assertEquals(timestamp, actual.getTimestamp().longValue());
        assertEquals(value, actual.getValue().intValue());
    }

}
