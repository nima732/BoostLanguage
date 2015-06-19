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
		second = (int) (time / 1000);
		if (second < 60) {
			return "Next alarm will be in " + days + " days, " + hours + " hours, " + minuts
					+ " minutes, " + second + " Sencod." ;
		} else {
			minuts = (int) (second / 60);
			second = (int) (time % 1000);
			if (minuts < 60) {
				return "Next alarm will be in " + days + " days, " + hours + " hours, " + minuts
						+ " minutes, " + second + " Sencod." ;
			} else {
				hours = minuts / 60;
				minuts =  (int) (second % 60);
				if (hours < 24){

					return "Next alarm will be in " + days + " days, " + hours + " hours, " + minuts
							+ " minutes, " + second + " Sencod." ;

				}else{
					days = hours / 24;
					hours = minuts % 60;
					if (days < 24){

						return "Next alarm will be in " + days + " days, " + hours + " hours, " + minuts
								+ " minutes, " + second + " Sencod." ;
						
					}
				}

			}

		}
		return "Next alarm will be in " + days + " days, " + hours + " hours, " + minuts
				+ " minutes, " + second + " Sencod." ;

	}
}
