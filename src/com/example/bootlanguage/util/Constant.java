package com.example.bootlanguage.util;

import android.util.Log;

public class Constant {

	private static long worldCounter = 0;
	
	public static long generateUniqeCounter(){
		Log.i("generateUniqeCounter", "Generate number is : " + worldCounter);
		return worldCounter++;
	}
}
