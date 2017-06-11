package org.diveintojee.poc.vintagezerodowntime.engineserver.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.diveintojee.poc.vintagezerodowntime.dto.MeasurementFactDTO;
import org.diveintojee.poc.vintagezerodowntime.engineserver.service.MeasurementFactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Created by <a href="mailto:louis.gueye@domo-safety.com">Louis Gueye</a>.
 */
@RestController
@RequestMapping("/api/providers/{provider}/facts")
public class HardwareProviderResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(HardwareProviderResource.class);
    @Autowired
    private MeasurementFactService service;

    /**
     *
     * @param provider
     * @param source
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MeasurementFactDTO> create(@PathVariable("provider") String provider, @RequestBody MeasurementFactDTO source) throws JsonProcessingException {
        MeasurementFactDTO dto = service.create(provider, source);
        LOGGER.debug("Saved heart rate fact {}", dto);
        // Build URI
        final URI location = ServletUriComponentsBuilder.fromCurrentServletMapping()
                .path("/api/facts/{businessId}").buildAndExpand(dto.getBusinessId())
                .toUri();
        LOGGER.debug("New resource can be found at : {}", location.toString());
        // Add uri location
        final HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        // Add header to response
        return new ResponseEntity<>(dto, headers, HttpStatus.CREATED);
    }

}
