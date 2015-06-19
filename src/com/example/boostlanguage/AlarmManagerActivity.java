package com.example.boostlanguage;

import java.io.IOException;

import com.example.boostlanguage.DAO.SQLiteHelper;
import com.example.boostlanguage.DAO.SentencesDAO;
import com.example.boostlanguage.DAO.SettingDAO;
import com.example.boostlanguage.entity.Sentences;
import com.example.boostlanguage.entity.Setting;
import com.example.bootlanguage.util.Constant;
import com.example.bootlanguage.util.ReminderUtility;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmManagerActivity extends Activity implements OnClickListener{

	private PowerManager.WakeLock wakeLock;
	SentencesDAO sentencesDAO;
	SettingDAO settingDAO;
	Sentences sentences;
	private Toast myToast;
	MediaPlayer mediaPlayer,mediaPlayerBeep;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sentences = null;

		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);

		Log.i("AlarmManagerActivity", "################################");

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		setContentView(R.layout.alarm);

		
		String insertedId = fetchIntent();

		sentences = findInDB(insertedId);
		
		setText(sentences);
		
		
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
				" My wake log");
		wakeLock.acquire();


		Button stopButton = (Button) findViewById(R.id.stopAlarm);
		stopButton.setOnClickListener(this);
		Button checkButton = (Button) findViewById(R.id.alarmCheck);
		checkButton.setOnClickListener(this);

//		playSound(this,getAlarmUri());
		playSoundBeep();
		
	}
	

	public void onStop() {
		super.onStop();
		try{
		wakeLock.release();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void playSound( Context context, Uri alert){
		mediaPlayer = new MediaPlayer();
		try{
			
			mediaPlayer.setDataSource(context, alert);
			AudioManager audioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
			
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0){
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mediaPlayer.prepare();
				mediaPlayer.start();
			}
			
		}catch(IOException ex){
			ex.printStackTrace();
			Log.i("AlarmManagerActivity", "No audio file are found!");
		}
	}
	
	private void prepareAlarm(Sentences sentences,long time ) {
		Log.i("AlarmManagerActivity", " @@@  time is " + time);
		Intent intent = new Intent(AlarmManagerActivity.this,
				AlarmManagerActivity.class);
		// For unspecified reason Extra will be deliver when action set (in
		// Pending thing).
		intent.setAction("SomeAction");

		Bundle extras = new Bundle();
		extras.putString("insertedId", String.valueOf(sentences.getId()));
		intent.putExtras(extras);
		// TODO find what means PendingIntent.FLAG_CANCEL_CURRENT
		PendingIntent pendingIntent = PendingIntent.getActivity(
				AlarmManagerActivity.this, (int)sentences.getId(), intent,
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
	}

	private String fetchIntent() {

		Bundle extras = getIntent().getExtras();
		String insertedId = "";

		if (extras != null) {
			insertedId = (String) extras.get("insertedId");
		}

		return insertedId;
	}

	// find the word from database by Id
	private Sentences findInDB(String insertedId) {
		Sentences sentences = null;

		sentencesDAO = new SentencesDAO(this);
		sentencesDAO.open();

		try {
			sentences = sentencesDAO.findById(Long.valueOf(insertedId));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.i("My test #@##### ", sentences.getWorld());

		return sentences;

	}

	private void setText(Sentences sentences) {
		
		
		TextView textView = (TextView) findViewById(R.id.alarmOrginTextId);
		textView.setText(sentences.getWorld());
	}

	private boolean check(Sentences sentences) {
		
		EditText editText = (EditText) findViewById(R.id.alarmEditTrans);
		String transText = editText.getText().toString();
		
		if (sentences.getWorldTrans().equalsIgnoreCase(transText)){
			return true;
		}
		
		return false;
	}

	private void createReminder(Sentences sentences) {
		if (check(sentences)){
//			showDialogCorrect(sentences, System.currentTimeMillis() + (long)(fetchSetting().getNumberCorrectDay() * 24 * 60 * 60 * 1000));
			prepareAlarm(sentences, System.currentTimeMillis() + (long)(fetchSetting().getNumberCorrectDay() * 24 * 60 * 60 * 1000));
		}else {
//			showDialogWrong(sentences, System.currentTimeMillis() + (long)(fetchSetting().getNumberWrongDay() * 24 * 60 * 60 * 1000));
			prepareAlarm(sentences , System.currentTimeMillis() + (long)(fetchSetting().getNumberWrongDay() * 24 * 60 * 60 * 1000));
		}
	}


	@Override
	public void onClick(View v) {
		
		switch (v.getId()){
		case  R.id.alarmCheck :
			Log.i("AlarmManagerActivity", " @@@ Check alarm @@@");
			createReminder(sentences);	
			if (mediaPlayer != null)
			mediaPlayer.stop();
			finish();
			
			break;
		case R.id.stopAlarm :
			Log.i("AlarmManagerActivity", " @@@ stop alarm @@@");
			if (mediaPlayer != null)
			mediaPlayer.stop();
			finish();
			
			break;
		}
	}
	
	private Setting fetchSetting(){
		Setting setting = null;
		
		settingDAO = new SettingDAO(this);
		settingDAO.open();
		
		try {
			setting = settingDAO.getAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return setting;
	}
	
	private void playSoundBeep(){
		mediaPlayerBeep = MediaPlayer.create(this, R.raw.beep_07);								// Using MediaPlayer to play sound. I have download a beep file to 
																							// play. Then put the file in new created folder -raw-.
		mediaPlayerBeep.setLooping(false);	
		Log.e("beep","started0");
		mediaPlayerBeep.start();
	}

	
	private Uri getAlarmUri(){
		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		if (alert == null){
			alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			if (alert == null){
				alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			}
		}
		return alert;
	}
	
	private void showDialogCorrect(final Sentences sentences,final long time){

		new AlertDialog.Builder(this)
	    .setTitle("Check Answer")
	    .setMessage("Your answer was correct next alarm would be at ")
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	
//	        	prepareAlarm(sentences,time);
	        
	        }
	     })
	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 

				Log.i("AlarmManagerActivity", " @@@ stop alarm @@@");
				if (mediaPlayer != null)
				mediaPlayer.stop();
				finish();

	        	
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();

	}

	
	private void showDialogWrong(final Sentences sentences,final long time){

		new AlertDialog.Builder(this)
	    .setTitle("Check Answer")
	    .setMessage("Your answer was wrong the correct answer is \""+sentences.getWorldTrans() +"\n nest alarm will be at " + ReminderUtility.convertTime(time))
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	
	        	prepareAlarm(sentences,time);

	        	
	        }
	     })
	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 

				Log.i("AlarmManagerActivity", " @@@ stop alarm @@@");
				if (mediaPlayer != null)
				mediaPlayer.stop();
				finish();

	        	
	        }
	     })
	    .setIcon(android.R.attr.alertDialogIcon)
	     .show();

	}

}
