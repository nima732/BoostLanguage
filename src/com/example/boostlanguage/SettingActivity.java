package com.example.boostlanguage;

import java.util.Calendar;

import com.example.boostlanguage.DAO.SentencesDAO;
import com.example.boostlanguage.DAO.SettingDAO;
import com.example.boostlanguage.entity.Setting;
import com.example.bootlanguage.util.ReminderUtility;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends Activity {

	SettingDAO settingDAO;
	SentencesDAO sentencesDAO;

	private DatePicker datePicker;
	private Calendar calendar;
	// private TextView dateView;
	private int year, month, day;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		settingDAO = new SettingDAO(this);
		settingDAO.open();

		sentencesDAO = new SentencesDAO(SettingActivity.this);
		sentencesDAO.open();

		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);

		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		showDate(year, month + 1, day);

	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.setting, menu);
	// return true;
	// }


	public void saveSetting(View view) throws Exception {

		EditText correctDay = (EditText) findViewById(R.id.settingEdtCorrect);
		EditText wrongDay = (EditText) findViewById(R.id.settingEdtWro);

		String tempNumberDayCorr = correctDay.getText().toString();
		String tempNumberDayWrong = wrongDay.getText().toString();

		if (!ReminderUtility.checkFormat(tempNumberDayCorr,
				ReminderUtility.numberOfday)) {

			Toast toastCorr = Toast.makeText(this,
					"Format of correct text is not correct !", 1500);
			toastCorr.show();
			return;

		}

		if (!ReminderUtility.checkFormat(tempNumberDayWrong,
				ReminderUtility.numberOfday)) {
			Toast toastWro = Toast.makeText(this,
					"Format of worng text is not correct !", 1500);
			toastWro.show();
			return;
		}

		float numberDay = (float) ReminderUtility
				.calcDevideMath(tempNumberDayCorr);
		float numberWrong = (float) ReminderUtility
				.calcDevideMath(tempNumberDayWrong);

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

		if (existantSetting == null) {
			settingDAO.insertRow(setting);
			toast = Toast.makeText(SettingActivity.this,
					"Your setting saved successfuly by inserting !", 1000);
			toast.show();

		} else {
			settingDAO.update(setting, existantSetting.getId());
			toast = Toast.makeText(SettingActivity.this,
					"Your setting saved successfuly by updating !", 1000);
			toast.show();

		}

	}

	public void settingClearData(View view) {

		new AlertDialog.Builder(this)
				.setTitle("Delete entry")
				.setMessage("Are you sure you want to delete all sentences?")
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								sentencesDAO.deleteAll();

							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing
							}
						}).setIcon(android.R.drawable.ic_dialog_alert).show();

	}

	@SuppressWarnings("deprecation")
	public void pickTime(View view) {
		showDialog(999);
		Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		if (id == 999) {
			return new DatePickerDialog(this, myDateListener, year, month, day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			// arg1 = year
			// arg2 = month
			// arg3 = day
			showDate(arg1, arg2 + 1, arg3);
		}
	};

	private void showDate(int year, int month, int day) {
		Toast.makeText(this, year + " " + month + " " + day, Toast.LENGTH_LONG);
		// dateView.setText(new StringBuilder().append(day).append("/")
		// .append(month).append("/").append(year));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
