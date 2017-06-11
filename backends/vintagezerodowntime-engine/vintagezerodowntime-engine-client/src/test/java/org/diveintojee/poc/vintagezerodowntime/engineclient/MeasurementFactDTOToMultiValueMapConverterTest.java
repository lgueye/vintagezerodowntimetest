package org.diveintojee.poc.vintagezerodowntime.engineclient;

import org.diveintojee.poc.vintagezerodowntime.dto.Measurement;
import org.diveintojee.poc.vintagezerodowntime.dto.MeasurementFactDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.Assert.assertEquals;

/**
 * Created by <a href="mailto:louis.gueye@domo-safety.com">Louis Gueye</a>.
 */
public class MeasurementFactDTOToMultiValueMapConverterTest {

    private MeasurementFactDTOToMultiValueMapConverter underTest;

    @Before
    public void before() {
        underTest = new MeasurementFactDTOToMultiValueMapConverter();
    }

    @Test
    public void convertShouldSucceed() throws Exception {
        // Given
        final Measurement measurement = Measurement.heart_rate;
        String deviceBusinessId = "any-bid";
        String provider = "any-provider";
        Integer value = 152;
        Long timestamp = 1488359698000L;
        MeasurementFactDTO source = MeasurementFactDTO.of(measurement, deviceBusinessId, provider, value, timestamp, null);

        // When
        MultiValueMap<String, String> actual = underTest.convert(source);

        // Then
        assertEquals(1, actual.get("measurement").size());
        assertEquals(measurement.name(), actual.get("measurement").get(0));
        assertEquals(1, actual.get("deviceBusinessId").size());
        assertEquals(deviceBusinessId, actual.get("deviceBusinessId").get(0));
        assertEquals(1, actual.get("provider").size());
        assertEquals(provider, actual.get("provider").get(0));
        assertEquals(1, actual.get("value").size());
        assertEquals(String.valueOf(value), actual.get("value").get(0));
        assertEquals(1, actual.get("timestamp").size());
        assertEquals(String.valueOf(timestamp), actual.get("timestamp").get(0));
    }

    @Test
    public void convertShouldReturnEmptyMap() throws Exception {
        // When
        MultiValueMap<String, String> actual = underTest.convert(null);

        // Then
        assertEquals(new LinkedMultiValueMap<>(), actual);
    }

}
