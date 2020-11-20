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

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class LoConfig {
	/**
	 * Configuration parameter
	 */
	public static class CfgParameter {
		public CfgParameter(String t, Object v) {
			this.t = t;
			this.v = v;
		}
		/**
		 * Configuration parameter type ("str", "bin", "f64", "u32" or "i32")
		 */
		public String t;

		/**
		 * Configuration parameter value
		 */
		public Object v;
	}

	/**
	 * current device configuration
	 */
	public final Map<String, CfgParameter> cfg = new HashMap<>();

	/**
	 * ID for update acks
	 */
	public Long cid = null;

	// Strange bug for u32 & i32 values : received as long and integer values but converted as Double by gson.fromJson()
	public static int toInt(Object o) {
		switch (o.getClass().getName()) {
			case "java.lang.Double":
				return ((Double) o).intValue();
			case "java.lang.Integer":
				return (Integer) o;
			case "java.lang.Float":
				return ((Float) o).intValue();
			case "java.lang.String":
				return Integer.parseInt((String) o);
			default:
				throw new IllegalArgumentException(o.getClass().getName());
		}
	}
	public static double toDouble(Object o) {
		switch (o.getClass().getName()) {
			case "java.lang.Double":
				return (Double) o;
			case "java.lang.Integer":
				return ((Integer) o).doubleValue();
			case "java.lang.Float":
				return ((Float) o).doubleValue();
			case "java.lang.String":
				return Double.parseDouble((String) o);
			default:
				throw new IllegalArgumentException(o.getClass().getName());
		}
	}
	public static String toString(Object o) {
		switch (o.getClass().getName()) {
			case "java.lang.Double":
			case "java.lang.Float":
				return o.toString();
			case "java.lang.Integer":
				return ((Integer) o).toString();
			case "java.lang.String":
				return (String) o;
			default:
				throw new IllegalArgumentException(o.getClass().getName());
		}
	}
}