package org.diveintojee.poc.vintagezerodowntime.engineclient;

import org.assertj.core.util.Lists;
import org.diveintojee.poc.vintagezerodowntime.dto.MeasurementFactDTO;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.diveintojee.poc.vintagezerodowntime.engineclient.EngineClient.ENGINE_API_URL_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * @author louis.gueye@gmail.com
 */
@SuppressWarnings("unchecked")
public class EngineClientTest {

	@Test(expected = IllegalStateException.class)
	public void createEngineClientShouldFailWithMissingRequiredSystemProperty() {
		System.setProperty(ENGINE_API_URL_KEY, "");
		new EngineClient();
	}

	@Test
	public void createMeasurementFact() throws Exception {
		// Given
		final String baseUrl = "http://foo.bar";
		System.setProperty(ENGINE_API_URL_KEY, baseUrl);
		EngineClient underTest = new EngineClient();
		RestTemplate httpClient = Mockito.mock(RestTemplate.class);
		underTest.setHttpClient(httpClient);

		final String provider = "any-provider";
		final String deviceBusinessId = "any-device-bid";

		final URI uri = ServletUriComponentsBuilder.fromHttpUrl(baseUrl + "/api/providers/" + provider + "/facts").build().toUri();
		ArgumentCaptor<HttpEntity> argumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);
		ResponseEntity<MeasurementFactDTO> response = Mockito.mock(ResponseEntity.class);
		when(httpClient.postForEntity(eq(uri), argumentCaptor.capture(), eq(MeasurementFactDTO.class))).thenReturn(response);
		MeasurementFactDTO dto = mock(MeasurementFactDTO.class);
		MeasurementFactDTO expected = MeasurementFactDTO.of(deviceBusinessId, provider);
		when(response.getBody()).thenReturn(expected);

		// When
		final MeasurementFactDTO actual = underTest.createMeasurementFact(provider, dto);

		// Then
		HttpEntity<MeasurementFactDTO> captured = argumentCaptor.getValue();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(APPLICATION_JSON);
		assertEquals(headers, captured.getHeaders());
		assertSame(dto, captured.getBody());
		assertSame(expected, actual);
	}

	@Test
	public void findMeasurementFactsByCriteria() throws Exception {
		// Given
		final String baseUrl = "http://foo.bar";
		System.setProperty(ENGINE_API_URL_KEY, baseUrl);
		EngineClient underTest = new EngineClient();
		RestTemplate httpClient = Mockito.mock(RestTemplate.class);
		underTest.setHttpClient(httpClient);

		MeasurementFactDTO criteria = new MeasurementFactDTO();
		criteria.setBusinessId("bid");
		criteria.setDeviceBusinessId("device-bid");
		criteria.setProvider("provider");
		criteria.setTimestamp(789L);
		criteria.setValue(180);
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("deviceBusinessId", criteria.getDeviceBusinessId());
		queryParams.add("provider", criteria.getProvider());
		queryParams.add("value", String.valueOf(criteria.getValue()));
		queryParams.add("timestamp", String.valueOf(criteria.getTimestamp()));
		URI uri = ServletUriComponentsBuilder.fromHttpUrl(baseUrl).path("/api/facts").queryParams(queryParams).build().toUri();
		ArgumentCaptor<HttpEntity> entityArgumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);
		ArgumentCaptor<ParameterizedTypeReference> responseTypeArgumentCaptor = ArgumentCaptor.forClass(ParameterizedTypeReference.class);
		ResponseEntity<List<MeasurementFactDTO>> response = Mockito.mock(ResponseEntity.class);
		when(httpClient.exchange(eq(uri), eq(HttpMethod.GET), entityArgumentCaptor.capture(), responseTypeArgumentCaptor.capture()))
				.thenReturn(response);
		final List<MeasurementFactDTO> expected = Lists.newArrayList();
		when(response.getBody()).thenReturn(expected);

		// When
		final List<MeasurementFactDTO> actual = underTest.findMeasurementFactsByCriteria(criteria);

		// Then
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(APPLICATION_FORM_URLENCODED);

		HttpEntity<MeasurementFactDTO> capturedEntity = entityArgumentCaptor.getValue();
		assertEquals(headers, capturedEntity.getHeaders());
		assertNull(capturedEntity.getBody());
		final ParameterizedTypeReference capturedResponseType = responseTypeArgumentCaptor.getValue();
		assertEquals(new ParameterizedTypeReference<List<MeasurementFactDTO>>() {}, capturedResponseType);
		assertSame(expected, actual);
	}

}
