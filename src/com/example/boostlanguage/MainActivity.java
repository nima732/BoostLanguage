package com.example.boostlanguage;

import java.util.List;

import com.example.boostlanguage.DAO.SentencesDAO;
import com.example.boostlanguage.DAO.SettingDAO;
import com.example.boostlanguage.entity.Sentences;
import com.example.boostlanguage.entity.Setting;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	private SentencesDAO sentencesDAO;
	private EditText sentencesText;
	private EditText transText;
	private Toast myToast;
	private SettingDAO settingDAO;

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

		List<Sentences> values = sentencesDAO.getAllSentences();

		// sentencesDAO.deleteAll();

		values = sentencesDAO.getAllSentences();
		// use the SimpleCursorAdapter to show the
		// elements in a ListView
		ArrayAdapter<Sentences> adapter = new ArrayAdapter<Sentences>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void saveWorld(View view){
		
		ArrayAdapter<Sentences> adapter = (ArrayAdapter<Sentences>) getListAdapter();
		Sentences sentences = null;
		
		String sentencesTXT =  sentencesText.getText().toString();
		String transTxt = transText.getText().toString();
		
		Log.i(" MainActivity ", sentencesTXT);
		Log.i(" MainActivity ", transTxt);

		sentences = new Sentences(sentencesTXT,transTxt);
		
		if (!ifSettingSets()){
			Toast toast = Toast.makeText(this, "Please first set the setting",
					1500);
			toast.show();
			return;
		}
		
		sentences = sentencesDAO.insertRow(sentences);
		adapter.add(sentences);
		
		prepareAlarm( sentences.getId());
		
		System.out.println("@@@@@@@");
	}

	public void goSetting(View view) {
		Intent intent = new Intent(MainActivity.this, SettingActivity.class);
		intent.setAction("ariseSetting");
		startActivity(intent);

	}

	private void prepareAlarm(long insertedId) {

		Intent intent = new Intent(MainActivity.this,
				AlarmManagerActivity.class);
		// For unspecific reason Extra will be deliver when action set (in
		// Pending thing).
		intent.setAction("SomeAction");
		// intent.putExtra("insertedId", insertedId);

		Bundle extras = new Bundle();
		extras.putString("insertedId", String.valueOf(insertedId));
		intent.putExtras(extras);
		// TODO find what means PendingIntent.FLAG_CANCEL_CURRENT
		PendingIntent pendingIntent = PendingIntent
				.getActivity(MainActivity.this, 2, intent,
						PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis() + 5000, pendingIntent);

		if (myToast != null) {
			myToast.cancel();
		}

		myToast = Toast.makeText(MainActivity.this, "set Alarm !!!", 1000);
		myToast.show();
	}

	private boolean ifSettingSets() {

		Setting setting = null;

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

}
