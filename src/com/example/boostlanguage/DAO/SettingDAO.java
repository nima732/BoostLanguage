package com.example.boostlanguage.DAO;

import java.util.ArrayList;
import java.util.List;

import com.example.boostlanguage.entity.Setting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SettingDAO {

	
	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;
	private String[] allColumns = { SQLiteHelper.COLUMN_NAME_ID_SETTING,
			SQLiteHelper.correct_answer, SQLiteHelper.wrong_answer };

	public SettingDAO(Context context) {
		Log.i(SettingDAO.class.getName(), ">>>>> SettingDAO");
		dbHelper = new SQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Setting findById(long id) throws Exception {

		int counter = 0;
		Setting setting = null;
		Cursor cursor = database.rawQuery("select * from "
				+ SQLiteHelper.TABLE_NAME_setting + " where "
				+ SQLiteHelper.COLUMN_NAME_ID_SETTING + " = " + id, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			if (counter > 1)
				throw new Exception("Db counter return more than two!");
			setting = cursorToSetting(cursor);
			cursor.moveToNext();
			counter++;
		}
		// make sure to close the cursor
		cursor.close();
		if (counter != 1)
			throw new Exception("Db return no resualt! ");

		return setting;
	}

	public Setting getAll() throws Exception {

		int counter = 0;
		Setting setting = null;
		Cursor cursor = database.rawQuery("select * from "
				+ SQLiteHelper.TABLE_NAME_setting , null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			if (counter > 1)
				throw new Exception("Db counter return more than two!");
			setting = cursorToSetting(cursor);
			cursor.moveToNext();
			counter++;
		}
		// make sure to close the cursor
		cursor.close();
		if (counter != 1)
			throw new Exception("Db return no resualt! ");

		return setting;
	}

	
	
//	public Setting createSetting(String world) {
//		Log.i(Setting.class.getName(), ">>>>> createSetting");
//		ContentValues values = new ContentValues();
//		values.put(SQLiteHelper.COLUMN_NAME_MAIN_SEN, world);
//		long insertId = database.insert(SQLiteHelper.TABLE_NAME, null, values);
//		Log.i("insertId : ", String.valueOf(insertId));
//		Cursor cursor = database.query(SQLiteHelper.TABLE_NAME, allColumns,
//				SQLiteHelper.COLUMN_NAME_ID + " = " + insertId, null, null,
//				null, null);
//		cursor.moveToFirst();
//		Sentences newSentence = cursorToSentences(cursor);
//		cursor.close();
//		return newSentence;
//	}

	public Setting insertRow(Setting setting) {
		Log.i(Setting.class.getName(), ">>>>> insert a row");
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.correct_answer, setting.getNumberCorrectDay());
		values.put(SQLiteHelper.wrong_answer, setting.getNumberWrongDay());
		long insertId = database.insert(SQLiteHelper.TABLE_NAME_setting, null, values);
		Log.i("insertId : ", String.valueOf(insertId));
		Cursor cursor = database.query(SQLiteHelper.TABLE_NAME_setting, allColumns,
				SQLiteHelper.COLUMN_NAME_ID_SETTING + " = " + insertId, null, null,
				null, null);
		cursor.moveToFirst();
		Setting newSetting = cursorToSetting(cursor);
		cursor.close();
		return newSetting;
	}
	
	public void update(Setting setting, long id){

		Log.i(Setting.class.getName(), ">>>>> update a row");
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.correct_answer, setting.getNumberCorrectDay());
		values.put(SQLiteHelper.wrong_answer, setting.getNumberWrongDay());
		long insertId = database.update(SQLiteHelper.TABLE_NAME_setting, values, SQLiteHelper.COLUMN_NAME_ID_SETTING + " = " + id ,null);
		
		
		Log.i("update : ", String.valueOf(insertId));
		Cursor cursor = database.query(SQLiteHelper.TABLE_NAME_setting, allColumns,
				SQLiteHelper.COLUMN_NAME_ID_SETTING + " = " + insertId, null, null,
				null, null);
		cursor.moveToFirst();
		Setting newSetting = cursorToSetting(cursor);
		cursor.close();

	}

//	public void deleteComment(Sentences sentences) {
//		long id = sentences.getId();
//		System.out.println("Comment deleted with id: " + id);
//		database.delete(SQLiteHelper.TABLE_NAME, SQLiteHelper.COLUMN_NAME_ID
//				+ " = " + id, null);
//	}

	public void deleteAll() {
		database.delete(SQLiteHelper.TABLE_NAME_setting, null, null);
	}

//	public List<Sentences> getAllSentences() {
//		List<Sentences> sentenceses = new ArrayList<Sentences>();
//
//		Cursor cursor = database.query(SQLiteHelper.TABLE_NAME, allColumns,
//				null, null, null, null, null);
//
//		cursor.moveToFirst();
//		while (!cursor.isAfterLast()) {
//			Sentences sentences = cursorToSentences(cursor);
//			sentenceses.add(sentences);
//			cursor.moveToNext();
//		}
//		// make sure to close the cursor
//		cursor.close();
//		return sentenceses;
//	}

	private Setting cursorToSetting(Cursor cursor) {
		Setting setting = new Setting();
		setting.setId(cursor.getLong(0));
		setting.setNumberCorrectDay(cursor.getFloat(1));
		setting.setNumberWrongDay(cursor.getFloat(2));
		return setting;
	}

}
