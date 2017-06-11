-- create nullable column
ALTER TABLE measurement_facts_timeline
  ADD COLUMN measurement VARCHAR(255);

-- set a value to all rows
UPDATE measurement_facts_timeline
SET measurement = 'heart_rate';

-- transform to non nullable column
ALTER TABLE measurement_facts_timeline
  ALTER COLUMN measurement SET NOT NULL;

-- create index on new column
CREATE INDEX idx_measurement_facts_timeline_measurement
  ON measurement_facts_timeline (measurement);
