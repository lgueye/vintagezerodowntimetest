package org.diveintojee.poc.vintagezerodowntime.dto;

import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.assertEquals;

/**
 * @author louis.gueye@gmail.com
 */
public class MeasurementFactDTOTest {

    @Test
    public void ofShouldSucceed() throws Exception {
        // Given
        String deviceBusinessId = "any-device-bid";
        String provider = "any-provider";
        Measurement measurement = Measurement.heart_rate;
        Integer value = 5;
        Long timestamp = Instant.now().toEpochMilli();
        String businessId = "any-bid";

        // When
        MeasurementFactDTO actual = MeasurementFactDTO.of(measurement, deviceBusinessId, provider, value, timestamp, businessId);

        // Then
        assertEquals(measurement, actual.getMeasurement());
        assertEquals(deviceBusinessId, actual.getDeviceBusinessId());
        assertEquals(provider, actual.getProvider());
        assertEquals(value, actual.getValue());
        assertEquals(timestamp, actual.getTimestamp());
    }

}
