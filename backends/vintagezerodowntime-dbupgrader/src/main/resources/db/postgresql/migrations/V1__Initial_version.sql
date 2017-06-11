DROP TABLE IF EXISTS measurement_facts_timeline CASCADE;

CREATE TABLE measurement_facts_timeline (
  measurement_fact_id  BIGSERIAL    NOT NULL,
  measurement_fact_bid VARCHAR(255) NOT NULL,
  device_bid          VARCHAR(255) NOT NULL,
  inserted_at         BIGINT       NOT NULL,
  provider            VARCHAR(255) NOT NULL,
  timestamp           INT8         NOT NULL,
  value               INT4         NOT NULL,
  PRIMARY KEY (measurement_fact_id)
);
CREATE INDEX idx_measurement_facts_timeline_measurement_fact_bid
  ON measurement_facts_timeline (measurement_fact_bid);
CREATE INDEX idx_measurement_facts_timeline_insat
  ON measurement_facts_timeline (inserted_at);

ALTER TABLE measurement_facts_timeline
  ADD CONSTRAINT idx_measurement_facts_timeline_uniq UNIQUE (measurement_fact_bid, inserted_at);
