package com.common.conf;

import java.util.ResourceBundle;

public class Configuration {
	private static ResourceBundle resource;
	private static int env;
	private static String envName;
	static{
		try {
			resource=ResourceBundle.getBundle("common");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		return resource.getString(key);
	}

	/**
	 * @param key
	 * @return
	 */
	public static String[] getArray(String key) {
		return resource.getStringArray(key);
	}

	/**
	 * @param key
	 * @return
	 */
	public static boolean containKey(String key) {
		return resource.containsKey(key);
	}
	
	/**
	 * @return
	 */

}
