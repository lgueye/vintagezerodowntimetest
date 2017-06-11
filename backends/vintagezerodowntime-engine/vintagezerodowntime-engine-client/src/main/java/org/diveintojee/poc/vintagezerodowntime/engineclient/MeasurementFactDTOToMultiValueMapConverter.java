package org.diveintojee.poc.vintagezerodowntime.engineclient;

import com.google.common.base.Strings;
import org.diveintojee.poc.vintagezerodowntime.dto.MeasurementFactDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Created by <a href="mailto:louis.gueye@domo-safety.com">Louis Gueye</a>.
 */
public class MeasurementFactDTOToMultiValueMapConverter implements Converter<MeasurementFactDTO, MultiValueMap<String, String>> {
	@Override
	public MultiValueMap<String, String> convert(MeasurementFactDTO source) {
		MultiValueMap<String, String> target = new LinkedMultiValueMap<>();
		if (source != null) {
			if (source.getMeasurement() != null) {
				target.add("measurement", source.getMeasurement().name());
			}
			if (!Strings.isNullOrEmpty(source.getDeviceBusinessId())) {
				target.add("deviceBusinessId", source.getDeviceBusinessId());
			}
			if (!Strings.isNullOrEmpty(source.getProvider())) {
				target.add("provider", source.getProvider());
			}
			if (source.getValue() != null) {
				target.add("value", String.valueOf(source.getValue()));
			}
			if (source.getTimestamp() != null) {
				target.add("timestamp", String.valueOf(source.getTimestamp()));
			}
		}
		return target;
	}
}
