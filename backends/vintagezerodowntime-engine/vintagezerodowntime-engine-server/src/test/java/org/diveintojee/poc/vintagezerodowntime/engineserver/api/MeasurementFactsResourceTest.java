package org.diveintojee.poc.vintagezerodowntime.engineserver.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.diveintojee.poc.vintagezerodowntime.dto.Measurement;
import org.diveintojee.poc.vintagezerodowntime.dto.MeasurementFactDTO;
import org.diveintojee.poc.vintagezerodowntime.engineserver.service.MeasurementFactService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class MeasurementFactsResourceTest {
    @Mock
    private MeasurementFactService service;
    @InjectMocks
    private MeasurementFactsResource underTest;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.standaloneSetup(underTest).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void searchShouldSucceed() throws Exception {
        final Measurement measurement = Measurement.heart_rate;
        final String deviceBusinessId = "device-bid";
        final String provider = "provider";
        MeasurementFactDTO example = new MeasurementFactDTO();
        example.setMeasurement(measurement);
        example.setDeviceBusinessId(deviceBusinessId);
        example.setProvider(provider);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("measurement", measurement.name());
        form.add("deviceBusinessId", deviceBusinessId);
        form.add("provider", provider);

        MeasurementFactDTO h0 = MeasurementFactDTO.of(measurement, deviceBusinessId, provider, 94, 78L, "b0");
        MeasurementFactDTO h1 = MeasurementFactDTO.of(measurement, deviceBusinessId, provider, 110, 125L, "b1");
        MeasurementFactDTO h2 = MeasurementFactDTO.of(measurement, deviceBusinessId, provider, 120, 178L, "b2");
        final List<MeasurementFactDTO> expected = Lists.newArrayList(h0, h1, h2);
        when(service.findByCriteria(example)).thenReturn(expected);

        // When
        String httpUrl = "http://foo.bar";
        mockMvc.perform(
                MockMvcRequestBuilders.get(UriComponentsBuilder.fromHttpUrl(httpUrl).path("/api/facts").queryParams(form).build().toUri())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().stringValues("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(Lists.newArrayList(expected))));
    }

    @Test
    public void loadShouldSucceed() throws Exception {
        // Given
        final Measurement measurement = Measurement.respiration_rate;
        final String deviceBusinessId = "device-bid";
        final String provider = "provider";
        final String businessId = "b0";
        MeasurementFactDTO expected = MeasurementFactDTO.of(measurement, deviceBusinessId, provider,  94, 78L, businessId);
        when(service.load(businessId)).thenReturn(expected);

        // When
        String httpUrl = "http://foo.bar";
        mockMvc.perform(
                MockMvcRequestBuilders.get(UriComponentsBuilder.fromHttpUrl(httpUrl).path("/api/facts/{businessId}").buildAndExpand(businessId).toUri())
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().stringValues("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expected)));
    }

}
