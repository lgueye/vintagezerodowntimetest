package org.diveintojee.poc.vintagezerodowntime.engineserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.diveintojee.poc.vintagezerodowntime.dto.Measurement;
import org.diveintojee.poc.vintagezerodowntime.dto.MeasurementFactDTO;
import org.diveintojee.poc.vintagezerodowntime.engineserver.converters.MeasurementFactDTOToMeasurementFactConverter;
import org.diveintojee.poc.vintagezerodowntime.engineserver.converters.MeasurementFactToMeasurementFactDTOConverter;
import org.diveintojee.poc.vintagezerodowntime.engineserver.domain.MeasurementFact;
import org.diveintojee.poc.vintagezerodowntime.engineserver.persistence.MeasurementFactRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class MeasurementFactServiceTest {

    @Mock
    private MeasurementFactRepository repository;
    @Mock
    private MeasurementFactDTOToMeasurementFactConverter dtoToDomainConverter;
    @Mock
    private MeasurementFactToMeasurementFactDTOConverter domainToDtoConverter;
    @Mock
    private SimpMessagingTemplate messagingTemplate;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private MeasurementFactService underTest;

    @Test
    public void createShouldSucceed() throws Exception {
        // Given
        String provider = "any-provider";
        final long timestamp = Instant.now().toEpochMilli();
        final String deviceBusinessId = "d-45879";
        MeasurementFactDTO input = mock(MeasurementFactDTO.class);
        Measurement measurement = Measurement.heart_rate;
        when(input.getMeasurement()).thenReturn(measurement);
        when(input.getProvider()).thenReturn(provider);
        when(input.getDeviceBusinessId()).thenReturn(deviceBusinessId);
        when(input.getTimestamp()).thenReturn(timestamp);
        MeasurementFact detached = mock(MeasurementFact.class);
        when(dtoToDomainConverter.convert(input)).thenReturn(detached);
        MeasurementFact persisted = mock(MeasurementFact.class);
        when(repository.save(detached)).thenReturn(persisted);
        final MeasurementFactDTO output = mock(MeasurementFactDTO.class);
        when(domainToDtoConverter.convert(persisted)).thenReturn(output);
        final String factPayload = "any-payload";
        when(objectMapper.writeValueAsString(input)).thenReturn(factPayload);
        final String businessId = String.format(MeasurementFactService.BUSINESSID_TEMPLATE, measurement.name(), deviceBusinessId, timestamp);
        final String destination = String.format(MeasurementFactService.DESTINATION_TEMPLATE, deviceBusinessId, measurement.name());

        // When
        final MeasurementFactDTO actual = underTest.create(provider, input);

        // Then
        verify(input).setProvider(provider);
        verify(input).setBusinessId(businessId);
        verify(detached).setInsertedAt(any(Long.class));
        verify(messagingTemplate).convertAndSend(destination, factPayload);
        assertSame(output, actual);
    }

    @Test
    public void findByCriteriaShouldSucceed() throws Exception {
        // Given
        String deviceBusinessId = "D-45678";
        String provider = "any-provider";
        Measurement measurement = Measurement.heart_rate;
        MeasurementFactDTO criteria = MeasurementFactDTO.of(measurement, deviceBusinessId, provider);
        MeasurementFact probe = MeasurementFact.of(null, deviceBusinessId, provider, null, null, null, null);
        when(dtoToDomainConverter.convert(criteria)).thenReturn(probe);
        Instant now = Instant.now();
        final long t0 = now.minusMillis(5).toEpochMilli();
        final long i0 = now.plusMillis(5).toEpochMilli();
        final String b0 = String.format(MeasurementFactService.BUSINESSID_TEMPLATE, measurement.name(), deviceBusinessId, t0);
        final int v0 = 97;
        MeasurementFact h0 = MeasurementFact.of(measurement, deviceBusinessId, provider, v0, t0, b0, i0);
        final long t1 = now.minusMillis(7).toEpochMilli();
        final long i1 = now.plusMillis(3).toEpochMilli();
        final String b1 = String.format(MeasurementFactService.BUSINESSID_TEMPLATE, measurement.name(), deviceBusinessId, t1);
        final int v1 = 121;
        MeasurementFact h1 = MeasurementFact.of(measurement, deviceBusinessId, provider, v1, t1, b1, i1);

        List<MeasurementFact> unsortedResults = Lists.newArrayList(h0, h1);
        ArgumentCaptor<Example> exampleArgumentCaptor = ArgumentCaptor.forClass(Example.class);
        when(repository.findAll(exampleArgumentCaptor.capture())).thenReturn(unsortedResults);

        final MeasurementFactToMeasurementFactDTOConverter converter = new MeasurementFactToMeasurementFactDTOConverter();
        final MeasurementFactDTO dto0 = converter.convert(h0);
        when(domainToDtoConverter.convert(h0)).thenReturn(dto0);
        final MeasurementFactDTO dto1 = converter.convert(h1);
        when(domainToDtoConverter.convert(h1)).thenReturn(dto1);
        // When
        final List<MeasurementFactDTO> actual = underTest.findByCriteria(criteria);

        // Then
        Example example = exampleArgumentCaptor.getValue();
        assertSame(probe, example.getProbe());
        assertEquals(Lists.newArrayList(dto1, dto0), actual);

    }

    @Test
    public void loadShouldSucceed() throws Exception {
        // Given
        String businessId = "any-bid";
        MeasurementFact persisted = mock(MeasurementFact.class);
        when(repository.findFirstByBusinessIdOrderByInsertedAtDesc(businessId)).thenReturn(persisted);
        MeasurementFactDTO expected = mock(MeasurementFactDTO.class);
        when(domainToDtoConverter.convert(persisted)).thenReturn(expected);
        // When
        final MeasurementFactDTO actual = underTest.load(businessId);

        // Then
        assertSame(expected, actual);
    }

}
