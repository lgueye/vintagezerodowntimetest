package org.diveintojee.poc.vintagezerodowntime.engineclient;

import com.google.common.base.Strings;
import org.diveintojee.poc.vintagezerodowntime.dto.MeasurementFactDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Created by <a href="mailto:louis.gueye@domo-safety.com">Louis Gueye</a>.
 */
public class EngineClient {

    static final String ENGINE_API_URL_KEY = "vintagezerodowntime.engine.server.api.url";
    private RestTemplate httpClient;
    private String apiUrl;
    private MeasurementFactDTOToMultiValueMapConverter measurementFactDTOToMultiValueMapConverter;

    void setHttpClient(RestTemplate httpClient) {
        this.httpClient = httpClient;
    }

    public EngineClient() {
        if (Strings.isNullOrEmpty(System.getProperty(ENGINE_API_URL_KEY))) {
            throw new IllegalStateException(
                    "Please provide the engine server api url as a system property to your jvm under key [" + ENGINE_API_URL_KEY + "]");
        }
        this.apiUrl = System.getProperty(ENGINE_API_URL_KEY);
        this.httpClient = new RestTemplate();
        measurementFactDTOToMultiValueMapConverter = new MeasurementFactDTOToMultiValueMapConverter();
    }

    public MeasurementFactDTO createMeasurementFact(String provider, MeasurementFactDTO dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        final HttpEntity<MeasurementFactDTO> request = new HttpEntity<>(dto, headers);
        final URI uri = ServletUriComponentsBuilder.fromHttpUrl(apiUrl).path("/api/providers/{provider}/facts").buildAndExpand(provider).encode().toUri();
        return httpClient.postForEntity(uri, request, MeasurementFactDTO.class).getBody();
    }

    public List<MeasurementFactDTO> findMeasurementFactsByCriteria(MeasurementFactDTO criteria) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> queryParams = measurementFactDTOToMultiValueMapConverter.convert(criteria);
        URI uri = ServletUriComponentsBuilder.fromHttpUrl(apiUrl).path("/api/facts")
                .queryParams(queryParams).build().toUri();
        final HttpEntity<Void> request = new HttpEntity<>(headers);
        return httpClient.exchange(uri, HttpMethod.GET, request, new ParameterizedTypeReference<List<MeasurementFactDTO>>() {
        }).getBody();

    }
}
