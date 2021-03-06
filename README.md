# LiveObjectsMqttDeviceSample
- Software Name : Live Objects Mqtt Device Sample
- Version: 1.0
- SPDX-FileCopyrightText: Copyright (c) 2019-2020 Orange Business Services
- SPDX-License-Identifier: BSD-3-Clause
- This software is distributed under the BSD-3-Clause,
the text of which is available at https://opensource.org/licenses/BSD-3-Clause
or see the "LICENCE" file for more details.
- Software description: Sample application for Orange Datavenue Live Objects <a>https://liveobjects.orange-business.com</a>


It is a device sample: it exchanges MQTT payloads with Live Objects as an MQTT device ('json+device' user name).
It can generate standard Live Objects messages as follows :<br>
```
	{
		"streamId":"device1stream",
		"timestamp":"2019-09-01T15:52:31.150Z",
		"location":{"lat":45.759723,"lon":4.84223},
		"model":"devtype1",
		"value":
		{
			"hygrometry": xx,
			"temperature": xx,
			"log": "xx"
		},
		"tags":["SampleTag"],
		"metadata":{"source":"urn:lo:nsid:samples:device1","connector":"mqtt"}
	}
```

It also handles Live-Objects-managed parameters, remote commands, and firmware updates.

This sample generates the same kind of payload as the Android app available at : 
<a>https://play.google.com/store/apps/details?id=com.orange.lo.assetdemo</a>
<br>


<h1> Installation notes </h1>

1) Create an account on Live Objects. You can get a free account (10 MQTT devices for 1 year) at : <a>https://liveobjects.orange-business.com/#/request_account</a> <br>
Don't check "Lora".

2) Generate your Device API key : menu Configuration/API Keys (<a>https://liveobjects.orange-business.com/#/config/apikeys</a>) click on "Add", select 'MQTT device' profile rights.

3) Paste the key in MyDevice.java, API_KEY variable. Adapt other neighbour constants to your needs.

4) Build as a standard Maven project into you IDE (eg Idea, Eclipse, ...).


