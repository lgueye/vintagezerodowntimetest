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
public class MeasurementFactDTOToMeasurementFactConverterTest {

    private MeasurementFactDTOToMeasurementFactConverter underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new MeasurementFactDTOToMeasurementFactConverter();
    }

    @Test
    public void convertShouldSucceed() throws Exception {
        // Given
        MeasurementFactDTO source = new MeasurementFactDTO();
        final String businessId = "bid";
        source.setBusinessId(businessId);
        final String deviceBusinessId = "device-bid";
        source.setDeviceBusinessId(deviceBusinessId);
        final String provider = "provider";
        source.setProvider(provider);
        final int value = 121;
        source.setValue(value);
        final long timestamp = Instant.now().toEpochMilli();
        source.setTimestamp(timestamp);

        // When
        final MeasurementFact actual = underTest.convert(source);

        // Then
        assertEquals(businessId, actual.getBusinessId());
        assertEquals(deviceBusinessId, actual.getDeviceBusinessId());
        assertEquals(provider, actual.getProvider());
        assertEquals(timestamp, actual.getTimestamp().longValue());
        assertEquals(value, actual.getValue().intValue());
    }

}
