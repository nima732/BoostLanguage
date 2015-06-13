package com.example.bootlanguage.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReminderUtility {

	public static final String numberOfday = "(\\d)+(\\/)?(\\d)*";

	public static boolean checkFormat(String str, String patternRegex) throws Exception {

		Pattern pattern = Pattern.compile(patternRegex);

		return pattern.matches(patternRegex, str);
	}

	public static float calcDevideMath(String str) throws Exception{
		long firstPart,secondPart;
		try{
			firstPart = Long.parseLong((String) str.subSequence(0, str.indexOf("/")));
			secondPart= Long.parseLong((String) str.subSequence(str.indexOf("/") +1 , str.length()));
		}catch (Exception ex){
			throw ex;
		}
		return (float)firstPart / secondPart;
	}
}
