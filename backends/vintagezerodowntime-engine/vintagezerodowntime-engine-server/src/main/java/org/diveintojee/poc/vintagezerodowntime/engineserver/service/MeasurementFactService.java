package org.diveintojee.poc.vintagezerodowntime.engineserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.diveintojee.poc.vintagezerodowntime.dto.MeasurementFactDTO;
import org.diveintojee.poc.vintagezerodowntime.engineserver.converters.MeasurementFactDTOToMeasurementFactConverter;
import org.diveintojee.poc.vintagezerodowntime.engineserver.converters.MeasurementFactToMeasurementFactDTOConverter;
import org.diveintojee.poc.vintagezerodowntime.engineserver.domain.MeasurementFact;
import org.diveintojee.poc.vintagezerodowntime.engineserver.persistence.MeasurementFactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class MeasurementFactService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementFactService.class);
    static final String BUSINESSID_TEMPLATE = "%s-%s-%s";
    static final String DESTINATION_TEMPLATE = "/topic/%s/%s";
    @Autowired
    private MeasurementFactRepository repository;
    @Autowired
    private MeasurementFactDTOToMeasurementFactConverter dtoToDomainConverter;
    @Autowired
    private MeasurementFactToMeasurementFactDTOConverter domainToDtoConverter;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public MeasurementFactDTO create(String provider, MeasurementFactDTO dto) throws JsonProcessingException {
        dto.setProvider(provider);
        final String measurement = dto.getMeasurement().name();
        final String deviceBusinessId = dto.getDeviceBusinessId();
        final String businessId = String.format(BUSINESSID_TEMPLATE, measurement, deviceBusinessId, dto.getTimestamp());
        dto.setBusinessId(businessId);
        final MeasurementFact detached = dtoToDomainConverter.convert(dto);
        final long insertedAt = Instant.now().toEpochMilli();
        detached.setInsertedAt(insertedAt);
        final MeasurementFact persisted = repository.save(detached);
        LOGGER.debug("Saved fact {}", persisted);
        final String factPayload = objectMapper.writeValueAsString(dto);
        final String destination = String.format(DESTINATION_TEMPLATE, deviceBusinessId, measurement);
        messagingTemplate.convertAndSend(destination, factPayload);
        LOGGER.debug("Sent fact {} to {}", factPayload, destination);
        return domainToDtoConverter.convert(persisted);
    }

    public List<MeasurementFactDTO> findByCriteria(MeasurementFactDTO criteria) {
        final MeasurementFact example = dtoToDomainConverter.convert(criteria);
        final List<MeasurementFact> measurementFacts = repository.findAll(Example.of(example));
        LOGGER.debug("Found {} facts for criteria {}", measurementFacts.size(), example);

        return measurementFacts.stream()
                // find all
                .collect(Collectors.groupingBy(MeasurementFact::getBusinessId))
                // group by account
                .values().stream()
                .map(factsList -> factsList.stream().sorted(Comparator.comparing(MeasurementFact::getInsertedAt).reversed()) // order
                        // by
                        // inserted
                        // at desc
                        .findFirst().orElse(null))
                // pick first
                .sorted(Comparator.comparing(MeasurementFact::getInsertedAt))
                .map(domain -> domainToDtoConverter.convert(domain))
                .collect(Collectors.toList());

    }

    public MeasurementFactDTO load(String businessId) {
        return domainToDtoConverter.convert(repository.findFirstByBusinessIdOrderByInsertedAtDesc(businessId));
    }
}
