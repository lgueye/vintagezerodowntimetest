package org.diveintojee.poc.vintagezerodowntime.engineserver.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.diveintojee.poc.vintagezerodowntime.dto.Measurement;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "measurement_facts_timeline", indexes = {@Index(name = "idx_measurement_facts_timeline_measurement_fact_bid", columnList = "measurement_fact_bid"),
		@Index(name = "idx_measurement_facts_timeline_insat", columnList = "inserted_at"),
		@Index(name = "idx_measurement_facts_timeline_measurement", columnList = "measurement"),
		@Index(name = "idx_measurement_facts_timeline_uniq", columnList = "measurement_fact_bid,inserted_at", unique = true)})
public class MeasurementFact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "measurement_fact_id")
	private Long id;

	@Column(name = "inserted_at", columnDefinition = "BIGINT", nullable = false)
	@NotNull
	private Long insertedAt;

	@Column(name = "measurement_fact_bid", nullable = false)
	@NotNull
	private String businessId;

	@Column(name = "device_bid", nullable = false)
	@NotNull
	private String deviceBusinessId;

	@Column(nullable = false)
	@NotNull
	@Enumerated(EnumType.STRING)
	private Measurement measurement;

	@Column(nullable = false)
	@NotNull
	private String provider;

	@Column(nullable = false)
	@NotNull
	private Integer value;

	@Column(nullable = false)
	@NotNull
	private Long timestamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getInsertedAt() {
		return insertedAt;
	}

	public void setInsertedAt(Long insertedAt) {
		this.insertedAt = insertedAt;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getDeviceBusinessId() {
		return deviceBusinessId;
	}

	public void setDeviceBusinessId(String deviceBusinessId) {
		this.deviceBusinessId = deviceBusinessId;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
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
		MeasurementFact that = (MeasurementFact) o;
		return Objects.equal(insertedAt, that.insertedAt) &&
				Objects.equal(businessId, that.businessId);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(insertedAt, businessId);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("id", id)
				.add("insertedAt", insertedAt)
				.add("businessId", businessId)
				.add("deviceBusinessId", deviceBusinessId)
				.add("measurement", measurement)
				.add("provider", provider)
				.add("value", value)
				.add("timestamp", timestamp)
				.toString();
	}

	public static MeasurementFact of(Measurement measurement, String deviceBusinessId, String provider, Integer value, Long timestamp, String businessId, Long instant) {
		MeasurementFact fact = new MeasurementFact();
		fact.setMeasurement(measurement);
		fact.setDeviceBusinessId(deviceBusinessId);
		fact.setProvider(provider);
		fact.setValue(value);
		fact.setTimestamp(timestamp);
		fact.setBusinessId(businessId);
		fact.setInsertedAt(instant);
		return fact;
	}
}
