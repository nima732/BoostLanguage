package com.example.bootlanguage.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReminderUtility {

	public static final String numberOfday = "(\\d)+(\\/)?(\\d)*";

	public static boolean checkFormat(String str, String patternRegex)
			throws Exception {

		Pattern pattern = Pattern.compile(patternRegex);

		return pattern.matches(patternRegex, str);
	}

	public static float calcDevideMath(String str) throws Exception {
		long firstPart, secondPart;
		try {
			firstPart = Long.parseLong((String) str.subSequence(0,
					str.indexOf("/")));
			secondPart = Long.parseLong((String) str.subSequence(
					str.indexOf("/") + 1, str.length()));
		} catch (Exception ex) {
			throw ex;
		}
		return (float) firstPart / secondPart;
	}

	public static String convertTime(long time) {
		time = time - System.currentTimeMillis();
		int days = 0;
		int hours = 0;
		int minuts = 0;
		int second = 0;
		float milSecond = time / 1000;
		if (milSecond < 60) {
			return "Next alarm will be in " + days + ", " + minuts
					+ " minutes, " + second + " Sencod, " + milSecond
					+ " milSecond.";
		} else {
			second = (int) (milSecond / 60);

			if (second < 60) {
				return "Next alarm will be in " + days + ", " + minuts
						+ " minutes, " + second + " Sencod, " + milSecond
						+ " milSecond.";
			} else {
				minuts = second / 60;
				if (minuts < 24){

					return "Next alarm will be in " + days + ", " + minuts
							+ " minutes, " + second + " Sencod, " + milSecond
							+ " milSecond.";

				}else{
					hours = minuts / 24;
					
					if (hours < 24){

						return "Next alarm will be in " + days + ", " + minuts
								+ " minutes, " + second + " Sencod, " + milSecond
								+ " milSecond.";
						
					}else{
						days = hours -24;
						
						return "Next alarm will be in " + days + ", " + minuts
								+ " minutes, " + second + " Sencod, " + milSecond
								+ " milSecond.";

					}
				}

			}

		}
	}
}
