package org.diveintojee.poc.vintagezerodowntime.engineserver.converters;

import org.diveintojee.poc.vintagezerodowntime.dto.MeasurementFactDTO;
import org.diveintojee.poc.vintagezerodowntime.engineserver.domain.MeasurementFact;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by <a href="mailto:louis.gueye@domo-safety.com">Louis Gueye</a>.
 */
@Component
public class MeasurementFactDTOToMeasurementFactConverter implements Converter<MeasurementFactDTO, MeasurementFact> {

    @Override
    public MeasurementFact convert(MeasurementFactDTO source) {
        if (source == null) return null;
        MeasurementFact target = new MeasurementFact();
        target.setMeasurement(source.getMeasurement());
        target.setBusinessId(source.getBusinessId());
        target.setDeviceBusinessId(source.getDeviceBusinessId());
        target.setProvider(source.getProvider());
        target.setTimestamp(source.getTimestamp());
        target.setValue(source.getValue());
        return target;
    }
}
