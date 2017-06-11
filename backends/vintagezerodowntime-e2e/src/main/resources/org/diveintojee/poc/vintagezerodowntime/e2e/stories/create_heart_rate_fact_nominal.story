Meta:
@heart_rate

Narrative:
As a facts engine processor
I want to process heart rate facts

Scenario: engine should process heart rate facts

When provider medrate sends the following facts:
|measurement|deviceBusinessId|value|timestamp|
|heart_rate|D-8563461|171|1488542532000|
|heart_rate|D-8563461|172|1488542534000|
|heart_rate|D-8563461|165|1488542536000|
|heart_rate|D-8563461|167|1488542538000|
|heart_rate|D-8563461|167|1488542540000|
|heart_rate|D-8563461|162|1488542542000|
|heart_rate|D-8563461|175|1488542544000|

And consumer searchs for facts with the following criteria:
|measurement|provider|deviceBusinessId|
|heart_rate|medrate|D-8563461|

Then the following facts were persisted:
|measurement|provider|businessId|deviceBusinessId|value|timestamp|
|heart_rate|medrate|heart_rate-D-8563461-1488542532000|D-8563461|171|1488542532000|
|heart_rate|medrate|heart_rate-D-8563461-1488542534000|D-8563461|172|1488542534000|
|heart_rate|medrate|heart_rate-D-8563461-1488542536000|D-8563461|165|1488542536000|
|heart_rate|medrate|heart_rate-D-8563461-1488542538000|D-8563461|167|1488542538000|
|heart_rate|medrate|heart_rate-D-8563461-1488542540000|D-8563461|167|1488542540000|
|heart_rate|medrate|heart_rate-D-8563461-1488542542000|D-8563461|162|1488542542000|
|heart_rate|medrate|heart_rate-D-8563461-1488542544000|D-8563461|175|1488542544000|
