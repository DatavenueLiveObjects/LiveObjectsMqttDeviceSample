package com.orange.mqttDeviceModePublishData.jsonpatterns;

/*
 * Copyright (C) 2019 Orange Business Services
 *
 * This software is distributed under the terms and conditions of the '3-Clause BSD'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/BSD-3-Clause'.
 */

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