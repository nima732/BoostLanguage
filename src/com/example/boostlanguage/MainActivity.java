package com.example.boostlanguage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.boostlanguage.DAO.SentencesDAO;
import com.example.boostlanguage.DAO.SettingDAO;
import com.example.boostlanguage.entity.Sentences;
import com.example.boostlanguage.entity.Setting;
import com.example.bootlanguage.util.Constant;
import com.example.bootlanguage.util.ListViewAdapters;
import com.example.bootlanguage.util.ReminderUtility;

import android.os.Bundle;
import android.R.anim;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	private SentencesDAO sentencesDAO;
	private EditText sentencesText;
	private EditText transText;
	private Toast myToast;
	private SettingDAO settingDAO;
	Setting setting = null;
	ListView listView;

	private ArrayList<HashMap<String, String>> hashMaps;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sentencesDAO = new SentencesDAO(this);
		sentencesDAO.open();
		settingDAO = new SettingDAO(this);
		settingDAO.open();

		sentencesText = (EditText) findViewById(R.id.sentensTxt);
		transText = (EditText) findViewById(R.id.transTXT);

		listView=(ListView)findViewById(android.R.id.list);
		
		
		List<Sentences> values = sentencesDAO.getAllSentences();

		// sentencesDAO.deleteAll();

		values = sentencesDAO.getAllSentences();
		
		for (Sentences sentences : values){
			System.out.println(" %%%%%%%<<<%%%%"+ ifAlarmExist(sentences));
			if (!ifAlarmExist(sentences)){
				long isValidTime = sentences.getTime() - System.currentTimeMillis();
				if (isValidTime > 0){
					prepareAlarm(sentences, sentences.getTime());
				}
			}
			
		}

		
		hashMaps=new ArrayList<HashMap<String,String>>();
		
		ReminderUtility.convertlistToHashMap(hashMaps, values);
		// use the SimpleCursorAdapter to show the
		// elements in a ListView
//		ArrayAdapter<Sentences> adapter = new ArrayAdapter<Sentences>(this,
//				android.R.layout.simple_list_item_1, values);
//		setListAdapter(adapter);
		
		ListViewAdapters adapter=new ListViewAdapters(this, hashMaps);
		listView.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void saveWorld(View view){
		
//		ArrayAdapter<Sentences> adapter = (ArrayAdapter<Sentences>) getListAdapter();
		
		
		Sentences sentences = null;
				
		if (!ifSettingSets()){
			Toast toast = Toast.makeText(this, "Please first set the setting",
					1500);
			toast.show();
			return;
		}

		
		long time = System.currentTimeMillis() + (long)(setting.getNumberWrongDay() * 24 * 60 * 60 * 1000);
		
		String sentencesTXT =  sentencesText.getText().toString();
		String transTxt = transText.getText().toString();
		
		Log.i(" MainActivity ", sentencesTXT);
		Log.i(" MainActivity ", transTxt);

		sentences = new Sentences(sentencesTXT,transTxt,time);
		
		
		sentences = sentencesDAO.insertRow(sentences);
		
		listView=(ListView)findViewById(android.R.id.list);
		ListViewAdapters adapter2 = (ListViewAdapters) listView.getAdapter();

		HashMap<String,String> temp=new HashMap<String, String>();
		temp.put(Constant.FIRST_COLUMN, sentences.getWorld());
		temp.put(Constant.SECOND_COLUMN, sentences.getWorldTrans());
		temp.put(Constant.THIRD_COLUMN, String.valueOf(ReminderUtility.showReadableTime(sentences.getTime())));

		
		adapter2.add(temp);
		
		prepareAlarm( sentences,time );
		
		System.out.println("@@@@@@@");
	}

	public void goSetting(View view) {
		Intent intent = new Intent(MainActivity.this, SettingActivity.class);
		intent.setAction("ariseSetting");
		startActivity(intent);

	}

	// TODO this preparAlarm and the other one in AlarmManagerActivity should become one.
	private void prepareAlarm(Sentences sentences,long time) {
		 
		
		Intent intent = new Intent(MainActivity.this,
				AlarmManagerActivity.class);
		/* For unspecific reason Extra will be deliver 
		 * when action set (inPending thing).
		*/
		intent.setAction("SomeAction");

		Bundle extras = new Bundle();
		extras.putString("insertedId", String.valueOf(sentences.getId()));
		intent.putExtras(extras);
		
		/*
		 *PendingIntent.FLAG_CANCEL_CURRENT means if there is another
		 *same PendingIntent cancel that. To have many different alarm it should provide 
		 *different key. It has been done by id in sentence because that is uniqe.
		 */
		PendingIntent pendingIntent = PendingIntent
				.getActivity(MainActivity.this, (int)sentences.getId(), intent,
						PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP,
				time, pendingIntent);

		sentences.setTime(time);
		sentencesDAO.updateRows(sentences);
		
		if (myToast != null) {
			myToast.cancel();
		}

		myToast = Toast.makeText(MainActivity.this, "set Alarm !!! Next alarm is at"+ ReminderUtility.convertTime(time), Toast.LENGTH_LONG);
		myToast.show();
	}

	private boolean ifSettingSets() {

		try {
			setting = settingDAO.getAll();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (setting == null) {
			return false;
		}
		return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
 
		Log.i("MainActivity", "%%%%%%%%%%%%");

		if (!ifSettingSets()){
			Toast toast = Toast.makeText(this, "Please first set the setting",
					1500);
			toast.show();
			return;
		}
		
		//get selected items
//		Sentences sentences =  (Sentences) getListAdapter().getItem(position);
		Sentences sentences =  ReminderUtility.convertHashMapToSentences((HashMap<String, String>) listView.getAdapter().getItem(position));
		
		
		dialogSetAlarm(sentences);
	}
	
	public void dialogSetAlarm(final Sentences sentences){
		new AlertDialog.Builder(this)
	    .setTitle("Reset alarm")
	    .setMessage("Are you sure you want to reset the alarm for " + sentences.getWorld())
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 

	        	long time = System.currentTimeMillis() + (long)(setting.getNumberWrongDay() * 24 * 60 * 60 * 1000);
	        	prepareAlarm(sentences,time);
	        	onResume();
	        }
	     })
	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
		
	}

	@Override
	public void onResume(){
		super.onResume();
		
		listView=(ListView)findViewById(android.R.id.list);
		
		
		List<Sentences> values = sentencesDAO.getAllSentences();


		values = sentencesDAO.getAllSentences();
				
		hashMaps=new ArrayList<HashMap<String,String>>();
		
		ReminderUtility.convertlistToHashMap(hashMaps, values);
		
		ListViewAdapters adapter=new ListViewAdapters(this, hashMaps);
		listView.setAdapter(adapter);

	}
	
	private boolean ifAlarmExist(Sentences sentences){
		
		Intent intent = new Intent(MainActivity.this,
				AlarmManagerActivity.class);
		/* For unspecific reason Extra will be deliver 
		 * when action set (inPending thing).
		*/
		intent.setAction("SomeAction");

		Bundle extras = new Bundle();
		extras.putString("insertedId", String.valueOf(sentences.getId()));
		intent.putExtras(extras);
		
		boolean exist = (PendingIntent
				.getActivity(MainActivity.this, (int)sentences.getId(), intent,
						PendingIntent.FLAG_NO_CREATE) != null);
		if (exist) {
			return exist;
		}else {
			return (PendingIntent
					.getActivity(MainActivity.this, (int)sentences.getId(), intent,
							PendingIntent.FLAG_NO_CREATE) != null);
		}
	}
	
}
