package org.diveintojee.poc.vintagezerodowntime.engineserver.api;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.util.UriComponentsBuilder;

import static org.mockito.Mockito.when;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class HardwareProviderResourceTest {
    @Mock
    private MeasurementFactService service;
    @InjectMocks
    private HardwareProviderResource underTest;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.standaloneSetup(underTest).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void createShouldSucceed() throws Exception {
        // Given
        final String deviceBusinessId = "device-bid";
        final String provider = "provider";
        final String businessId = "b0";
        MeasurementFactDTO source = MeasurementFactDTO.of(deviceBusinessId, provider,  94, 78L, businessId);
        MeasurementFactDTO expected = MeasurementFactDTO.of(deviceBusinessId, provider,  94, 78L, businessId);;
        when(service.create(provider, source)).thenReturn(expected);

        // When
        String httpUrl = "http://foo.bar";
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post(UriComponentsBuilder.fromHttpUrl(httpUrl).path("/api/providers/{provider}/facts")
                                .buildAndExpand(provider).toUri()).content(objectMapper.writeValueAsString(source))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(
                        MockMvcResultMatchers.header().string("Location",
                                httpUrl + "/api/facts/" + businessId))
                .andExpect(MockMvcResultMatchers.header().stringValues("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expected)));

    }

}
