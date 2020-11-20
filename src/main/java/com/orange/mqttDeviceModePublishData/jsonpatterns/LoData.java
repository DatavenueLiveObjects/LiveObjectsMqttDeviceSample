/*
 * Software Name : Live Objects Mqtt Device Sample
 * Version: 1.0
 * SPDX-FileCopyrightText: Copyright (c) 2019-2020 Orange Business Services
 * SPDX-License-Identifier: BSD-3-Clause
 * This software is distributed under the BSD-3-Clause,
 * the text of which is available at https://opensource.org/licenses/BSD-3-Clause
 * or see the "LICENCE" file for more details.
 * Software description: Sample application for Orange Datavenue Live Objects <a>https://liveobjects.orange-business.com</a>
 */

package com.orange.mqttDeviceModePublishData.jsonpatterns;

import java.util.List;

public class LoData {
	public String s;        // Stream identifier. Fill it to persist the data in dataZone
	public String ts;       // your timestamp (ISO8601 format). If null, it is filled by LiveObject
	public String m;        // fill if you want your data to be indexed by Elastic Search. Must be unique per data type.
	public Double[] loc;    // location of your device, if known
	public Object v;        // <-- Replace Object by your own data type. This is the structured value.
	public List<String> t;  // optional tags depending on your needs
}
