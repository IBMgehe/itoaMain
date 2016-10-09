package com.ibm.automation.core.util;

public class FloatToIntUtil {
	public static String floatToInt(String floatdata) {
		String temp = floatdata.substring(0,  floatdata.length()-1);
		return new String(Float.valueOf(temp).intValue() + "%");
	}

	
}
