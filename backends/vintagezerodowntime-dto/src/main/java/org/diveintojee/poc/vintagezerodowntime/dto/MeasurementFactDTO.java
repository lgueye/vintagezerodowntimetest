package org.diveintojee.poc.vintagezerodowntime.dto;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Created by <a href="mailto:louis.gueye@domo-safety.com">Louis Gueye</a>.
 */
public class MeasurementFactDTO {
    private Measurement measurement;
    private String businessId;
    private String deviceBusinessId;
    private String provider;
    private Integer value;
    private Long timestamp;

    public String getDeviceBusinessId() {
        return deviceBusinessId;
    }

    public void setDeviceBusinessId(String deviceBusinessId) {
        this.deviceBusinessId = deviceBusinessId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasurementFactDTO that = (MeasurementFactDTO) o;
        return measurement == that.measurement &&
                Objects.equal(businessId, that.businessId) &&
                Objects.equal(deviceBusinessId, that.deviceBusinessId) &&
                Objects.equal(provider, that.provider) &&
                Objects.equal(value, that.value) &&
                Objects.equal(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(measurement, businessId, deviceBusinessId, provider, value, timestamp);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("measurement", measurement)
                .add("businessId", businessId)
                .add("deviceBusinessId", deviceBusinessId)
                .add("provider", provider)
                .add("value", value)
                .add("timestamp", timestamp)
                .toString();
    }

    public static MeasurementFactDTO of(Measurement measurement, String deviceBusinessId, String provider) {
        MeasurementFactDTO dto = new MeasurementFactDTO();
        dto.setDeviceBusinessId(deviceBusinessId);
        dto.setMeasurement(measurement);
        dto.setProvider(provider);
        return dto;
    }

    public static MeasurementFactDTO of(Measurement measurement, String deviceBusinessId, String provider, int value, long timestamp, String businessId) {
        MeasurementFactDTO fact = MeasurementFactDTO.of(measurement, deviceBusinessId, provider);
        fact.setTimestamp(timestamp);
        fact.setValue(value);
        fact.setBusinessId(businessId);
        return fact;
    }
}
