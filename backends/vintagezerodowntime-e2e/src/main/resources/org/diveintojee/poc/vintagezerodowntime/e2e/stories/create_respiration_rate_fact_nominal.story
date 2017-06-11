Meta:
@respiration_rate

Narrative:
As a facts engine processor
I want to process respiration rate facts

Scenario: engine should process respiration rate facts

When provider medrate sends the following facts:
|measurement|deviceBusinessId|value|timestamp|
|respiration_rate|D-8563461|12|1488542532000|
|respiration_rate|D-8563461|13|1488542534000|
|respiration_rate|D-8563461|14|1488542536000|
|respiration_rate|D-8563461|12|1488542538000|
|respiration_rate|D-8563461|11|1488542540000|
|respiration_rate|D-8563461|15|1488542542000|
|respiration_rate|D-8563461|13|1488542544000|

And consumer searchs for facts with the following criteria:
|measurement|provider|deviceBusinessId|
|respiration_rate|medrate|D-8563461|

Then the following facts were persisted:
|measurement|provider|businessId|deviceBusinessId|value|timestamp|
|respiration_rate|medrate|respiration_rate-D-8563461-1488542532000|D-8563461|12|1488542532000|
|respiration_rate|medrate|respiration_rate-D-8563461-1488542534000|D-8563461|13|1488542534000|
|respiration_rate|medrate|respiration_rate-D-8563461-1488542536000|D-8563461|14|1488542536000|
|respiration_rate|medrate|respiration_rate-D-8563461-1488542538000|D-8563461|12|1488542538000|
|respiration_rate|medrate|respiration_rate-D-8563461-1488542540000|D-8563461|11|1488542540000|
|respiration_rate|medrate|respiration_rate-D-8563461-1488542542000|D-8563461|15|1488542542000|
|respiration_rate|medrate|respiration_rate-D-8563461-1488542544000|D-8563461|13|1488542544000|
