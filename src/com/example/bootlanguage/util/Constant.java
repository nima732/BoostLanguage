package com.example.bootlanguage.util;

import android.util.Log;

public class Constant {

	private static long worldCounter = 0;
	public static final String FIRST_COLUMN="First";
	public static final String SECOND_COLUMN="Second";
	public static final String THIRD_COLUMN="Third";
	public static final String COLUMN_ID="id";
	
	public static long generateUniqeCounter(){
		Log.i("generateUniqeCounter", "Generate number is : " + worldCounter);
		return worldCounter++;
	}
}
