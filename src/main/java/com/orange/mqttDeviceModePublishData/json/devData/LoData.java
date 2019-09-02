package com.orange.mqttDeviceModePublishData.json.devData;

import java.util.List;

public class LoData {
	public String s;        // fill it  to persist the data in dataZone
	public String ts;       // your timestamp, if null : filled by LiveObject
	public String m;        // fill if you want your data to be indexed par Elastic Search
	public Double[] loc;    // location of your device if known
	public Object v;        // <-- Replace Object by your own data type
	public List t;          // optional tags regarding your needs
}
