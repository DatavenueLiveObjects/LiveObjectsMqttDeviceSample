package com.orange.mqttDeviceModePublishData.json.devData;

import java.util.List;

public class LoData {
	public String s;        // Stream identifier. Fill it to persist the data in dataZone
	public String ts;       // your timestamp (ISO8601 format). If null, it is filled by LiveObject
	public String m;        // fill if you want your data to be indexed by Elastic Search. Must be unique per data type.
	public Double[] loc;    // location of your device, if known
	public Object v;        // <-- Replace Object by your own data type. This is the structured value.
	public List<String> t;  // optional tags depending on your needs
}
