package com.example.boostlanguage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.boostlanguage.DAO.SentencesDAO;
import com.example.boostlanguage.DAO.SettingDAO;
import com.example.boostlanguage.entity.Sentences;
import com.example.boostlanguage.entity.Setting;
import com.example.bootlanguage.util.ListViewAdapters;
import com.example.bootlanguage.util.ReminderUtility;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class NotificationAlarm extends ListActivity{

	private SentencesDAO sentencesDAO;
	private Toast myToast;
	private SettingDAO settingDAO;
	Setting setting = null;
	ListView listView;

	private ArrayList<HashMap<String, String>> hashMaps;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_alarm);
		
		sentencesDAO = new SentencesDAO(this);
		sentencesDAO.open();
		settingDAO = new SettingDAO(this);
		settingDAO.open();


		listView=(ListView)findViewById(android.R.id.list);
		
		
		List<Sentences> values = sentencesDAO.getAllNotificationSentences();

		
		hashMaps=new ArrayList<HashMap<String,String>>();
		
		ReminderUtility.convertlistToHashMap(hashMaps, values);
		
		ListViewAdapters adapter=new ListViewAdapters(this, hashMaps);
		listView.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notification_alarm, menu);
		return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		 
		Log.i("Notification", "%%%%%%%%%%%%");
		
		Sentences sentences =  ReminderUtility.convertHashMapToSentences((HashMap<String, String>) listView.getAdapter().getItem(position));

		prepareAlarm(sentences, System.currentTimeMillis());
	}
	
	
	private void prepareAlarm(Sentences sentences,long time) {
		 
		
		Intent intent = new Intent(NotificationAlarm.this,
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
//		PendingIntent pendingIntent = PendingIntent
//				.getActivity(MainActivity.this, (int)sentences.getId(), intent,
//						PendingIntent.FLAG_CANCEL_CURRENT);
//		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//		alarmManager.set(AlarmManager.RTC_WAKEUP,
//				time, pendingIntent);

		sentences.setTime(time);
		sentencesDAO.updateRows(sentences);
		
		startActivity(intent);
	}


}
