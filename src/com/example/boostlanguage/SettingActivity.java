package com.example.boostlanguage;

import com.example.boostlanguage.DAO.SentencesDAO;
import com.example.boostlanguage.DAO.SettingDAO;
import com.example.boostlanguage.entity.Setting;
import com.example.bootlanguage.util.ReminderUtility;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends Activity {

	SettingDAO settingDAO ;
	SentencesDAO sentencesDAO ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		settingDAO = new SettingDAO(this);
		settingDAO.open();
		
		sentencesDAO = new SentencesDAO(SettingActivity.this);
		sentencesDAO.open();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}
	
	public void saveSetting(View view) throws Exception{
		
		EditText correctDay =(EditText) findViewById(R.id.settingEdtCorrect);
		EditText wrongDay = (EditText) findViewById(R.id.settingEdtWro);
		
		String tempNumberDayCorr =  correctDay.getText().toString();
		String tempNumberDayWrong = wrongDay.getText().toString();
	
		if (!ReminderUtility.checkFormat(tempNumberDayCorr, ReminderUtility.numberOfday)){

			Toast toastCorr = Toast.makeText(this, "Format of correct text is not correct !", 1500);
			toastCorr.show();
			return ;
			
		}

		if (!ReminderUtility.checkFormat(tempNumberDayWrong, ReminderUtility.numberOfday)){
			Toast toastWro = Toast.makeText(this, "Format of worng text is not correct !", 1500);
			toastWro.show();
			return ;
		}

		
		float numberDay = (float)ReminderUtility.calcDevideMath(tempNumberDayCorr);
		float numberWrong = (float)ReminderUtility.calcDevideMath(tempNumberDayWrong);
		
		Setting setting = new Setting();
		setting.setNumberCorrectDay(numberDay);
		setting.setNumberWrongDay(numberWrong);
		
		Setting existantSetting = null;
		try {
			existantSetting = settingDAO.getAll();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Toast toast = null;
		
		if (existantSetting == null){
			settingDAO.insertRow(setting);
			 toast = Toast.makeText(SettingActivity.this, "Your setting saved successfuly by inserting !", 1000);
			toast.show();

		}else {
			settingDAO.update(setting, existantSetting.getId());
			 toast = Toast.makeText(SettingActivity.this, "Your setting saved successfuly by updating !", 1000);
			toast.show();

		}
		
	}
	
	public void settingClearData(View view){
		
		new AlertDialog.Builder(this)
	    .setTitle("Delete entry")
	    .setMessage("Are you sure you want to delete all sentences?")
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	
	        	sentencesDAO.deleteAll();
	        
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
	
	

}
