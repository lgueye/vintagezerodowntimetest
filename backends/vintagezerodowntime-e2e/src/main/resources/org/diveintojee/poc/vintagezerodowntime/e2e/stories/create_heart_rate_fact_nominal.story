Meta:
@heart_rate

Narrative:
As a facts engine processor
I want to process heart rate facts

Scenario: engine should process heart rate facts

When provider medrate sends the following facts:
|deviceBusinessId|value|timestamp|
|D-8563461|171|1488542532000|
|D-8563461|172|1488542534000|
|D-8563461|165|1488542536000|
|D-8563461|167|1488542538000|
|D-8563461|167|1488542540000|
|D-8563461|162|1488542542000|
|D-8563461|175|1488542544000|

And consumer searchs for facts with the following criteria:
|provider|deviceBusinessId|
|medrate|D-8563461|

Then the following facts were persisted:
|provider|businessId|deviceBusinessId|value|timestamp|
|medrate|D-8563461-1488542532000|D-8563461|171|1488542532000|
|medrate|D-8563461-1488542534000|D-8563461|172|1488542534000|
|medrate|D-8563461-1488542536000|D-8563461|165|1488542536000|
|medrate|D-8563461-1488542538000|D-8563461|167|1488542538000|
|medrate|D-8563461-1488542540000|D-8563461|167|1488542540000|
|medrate|D-8563461-1488542542000|D-8563461|162|1488542542000|
|medrate|D-8563461-1488542544000|D-8563461|175|1488542544000|
