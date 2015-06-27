package com.example.bootlanguage.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.boostlanguage.entity.Sentences;

public class ReminderUtility {

	public static final String numberOfday = "(\\d)+(\\/)?(\\d)*";

	public static int genRandom(int min, int max){
		return (int) (Math.random() * (max -min)); 
	}
	
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
		int kharjeGhesmat = 0;
		kharjeGhesmat = (int) (time / 1000);
		if (kharjeGhesmat < 60) {
			second = kharjeGhesmat;
			return "Next alarm will be in " + days + " days, " + hours + " hours, " + minuts
					+ " minutes, " + second + " Sencod." ;
		} else {
			second = (int) (time % 1000); 
			time = kharjeGhesmat;
			kharjeGhesmat = (int) (time / 60);
			
			if (kharjeGhesmat < 60) {
				minuts = kharjeGhesmat;
				
				return "Next alarm will be in " + days + " days, " + hours + " hours, " + minuts
						+ " minutes, " + second + " Sencod." ;
			} else {
				minuts =  (int) (time % 60);
				time = kharjeGhesmat;
				kharjeGhesmat = (int) (time / 60);
				
				if (kharjeGhesmat < 24){
					hours = kharjeGhesmat;
					
					return "Next alarm will be in " + days + " days, " + hours + " hours, " + minuts
							+ " minutes, " + second + " Sencod." ;

				}else{
					hours = (int) (time % 24);
					time = kharjeGhesmat;
					kharjeGhesmat = (int) (time / 24) ;
					
					
					if (days < 24){
						days = kharjeGhesmat;
						
						return "Next alarm will be in " + days + " days, " + hours + " hours, " + minuts
								+ " minutes, " + second + " Sencod." ;
						
					}
				}

			}

		}
		return "Next alarm will be in " + days + " days, " + hours + " hours, " + minuts
				+ " minutes, " + second + " Sencod." ;

	}
	
	public static void  setAlaram(Context context,Sentences sentences,long time ){

/*		
		Log.i("AlarmManagerActivity", " @@@  time is " + time);
		Intent intent = new Intent(context, context.class);
		// For unspecified reason Extra will be deliver when action set (in
		// Pending thing).
		intent.setAction("SomeAction");

		Bundle extras = new Bundle();
		extras.putString("insertedId", String.valueOf(sentences.getId()));
		intent.putExtras(extras);
		PendingIntent pendingIntent = PendingIntent.getActivity(
				context, (int)sentences.getId(), intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP,
				time, pendingIntent);

		if (myToast != null) {
			myToast.cancel();
		}

		myToast = Toast.makeText(AlarmManagerActivity.this, "Correct answer was " + sentences.getWorldTrans() + " "+ ReminderUtility.convertTime(time),
				2500);
		myToast.show();
*/		
	}
}
