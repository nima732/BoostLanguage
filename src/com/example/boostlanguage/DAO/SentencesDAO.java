package com.example.boostlanguage.DAO;

import java.util.ArrayList;
import java.util.List;

import com.example.boostlanguage.entity.Sentences;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SentencesDAO {

	// Database fields
	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;
	private String[] allColumns = { SQLiteHelper.COLUMN_NAME_ID,
			SQLiteHelper.COLUMN_NAME_MAIN_SEN, SQLiteHelper.COLUMN_NAME_TRANS , SQLiteHelper.COLUMN_NAME_TIME};

	public SentencesDAO(Context context) {
		Log.i(Sentences.class.getName(), ">>>>> SentencesDAO");
		dbHelper = new SQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Sentences findById(long id) throws Exception {

		int counter = 0;
		Sentences sentences = null;
		Cursor cursor = database.rawQuery("select * from "
				+ SQLiteHelper.TABLE_NAME + " where "
				+ SQLiteHelper.COLUMN_NAME_ID + " = " + id, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			if (counter > 1)
				throw new Exception("Db counter return more than two!");
			sentences = cursorToSentences(cursor);
			cursor.moveToNext();
			counter++;
		}
		// make sure to close the cursor
		cursor.close();
		if (counter != 1)
			throw new Exception("Db return no resualt! ");

		return sentences;
	}

//	TODO should add time 
	public Sentences createSentences(String world) {
		Log.i(Sentences.class.getName(), ">>>>> createSentences");
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_NAME_MAIN_SEN, world);
		long insertId = database.insert(SQLiteHelper.TABLE_NAME, null, values);
		Log.i("insertId : ", String.valueOf(insertId));
		Cursor cursor = database.query(SQLiteHelper.TABLE_NAME, allColumns,
				SQLiteHelper.COLUMN_NAME_ID + " = " + insertId, null, null,
				null, null);
		cursor.moveToFirst();
		Sentences newSentence = cursorToSentences(cursor);
		cursor.close();
		return newSentence;
	}

	public Sentences insertRow(Sentences sentences) {
		Log.i(Sentences.class.getName(), ">>>>> insert a row");
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_NAME_MAIN_SEN, sentences.getWorld());
		values.put(SQLiteHelper.COLUMN_NAME_TRANS, sentences.getWorldTrans());
		values.put(SQLiteHelper.COLUMN_NAME_TIME, sentences.getTime());
		long insertId = database.insert(SQLiteHelper.TABLE_NAME, null, values);
		Log.i("insertId : ", String.valueOf(insertId));
		Cursor cursor = database.query(SQLiteHelper.TABLE_NAME, allColumns,
				SQLiteHelper.COLUMN_NAME_ID + " = " + insertId, null, null,
				null, null);
		cursor.moveToFirst();
		Sentences newSentence = cursorToSentences(cursor);
		cursor.close();
		return newSentence;
	}

	public void deleteComment(Sentences sentences) {
		long id = sentences.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(SQLiteHelper.TABLE_NAME, SQLiteHelper.COLUMN_NAME_ID
				+ " = " + id, null);
	}

	public void deleteAll() {
		database.delete(SQLiteHelper.TABLE_NAME, null, null);
	}

	public void deleteAll(String notification) {
		database.delete(SQLiteHelper.TABLE_NAME_NOTIFI, null, null);
	}

	public List<Sentences> getAllSentences() {
		List<Sentences> sentenceses = new ArrayList<Sentences>();

		Cursor cursor = database.query(SQLiteHelper.TABLE_NAME, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Sentences sentences = cursorToSentences(cursor);
			sentenceses.add(sentences);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return sentenceses;
	}
	
	public void updateRows(Sentences sentences){
		
		Log.i(SentencesDAO.class.getName(), ">>>>> update a row " + sentences.getId());
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_NAME_MAIN_SEN, sentences.getWorld());
		values.put(SQLiteHelper.COLUMN_NAME_TRANS, sentences.getWorldTrans());
		values.put(SQLiteHelper.COLUMN_NAME_TIME, sentences.getTime());
		long insertId = database.update(SQLiteHelper.TABLE_NAME, values, SQLiteHelper.COLUMN_NAME_ID + " = " + sentences.getId() ,null);
		
		
		Log.i("update : ", String.valueOf(insertId));
		Cursor cursor = database.query(SQLiteHelper.TABLE_NAME, allColumns,
				SQLiteHelper.COLUMN_NAME_ID + " = " + insertId, null, null,
				null, null);
		cursor.moveToFirst();
//		Sentences updatedSentences = cursorToSentences(cursor);
		cursor.close();

//		return updatedSentences;
	}
	
	public List<Sentences> getAllNotificationSentences() {
		List<Sentences> sentenceses = new ArrayList<Sentences>();

		Cursor cursor = database.query(SQLiteHelper.TABLE_NAME_NOTIFI, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Sentences sentences = cursorToSentences(cursor);
			sentenceses.add(sentences);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		
		deleteAll("Notifications");
		return sentenceses;
	}
	
	public Sentences insertRowNotifi(Sentences sentences) {
		Log.i(Sentences.class.getName(), ">>>>> insert a Notifi");
		Cursor cursor = null;
		Sentences newSentence = null;
		
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_NAME_ID, sentences.getId());
		values.put(SQLiteHelper.COLUMN_NAME_MAIN_SEN, sentences.getWorld());
		values.put(SQLiteHelper.COLUMN_NAME_TRANS, sentences.getWorldTrans());
		values.put(SQLiteHelper.COLUMN_NAME_TIME, sentences.getTime());
		try {
		long insertId = database.insert(SQLiteHelper.TABLE_NAME_NOTIFI, null, values);
		Log.i("insertId : ", String.valueOf(sentences.getId()));
		cursor = database.query(SQLiteHelper.TABLE_NAME_NOTIFI, allColumns,
				SQLiteHelper.COLUMN_NAME_ID + " = " + sentences.getId(), null, null,
				null, null);
		cursor.moveToFirst();
		newSentence = cursorToSentences(cursor);
		}catch (SQLiteConstraintException ex){
//			ignore logging for constraint.
		}catch (Exception e) {
			e.printStackTrace();
		}
		cursor.close();
		return newSentence;
	}
	
	public long getCheckTimeConflict(long time){

		long maxTime = 0;
		int count = 0;
		
		Log.i("SentencesDAO", "&&&&111 &&&& " + maxTime);

		
		Cursor cursor = null;
		try{
			
			
		cursor = database.rawQuery("select * from "+ SQLiteHelper.TABLE_NAME + " where " 
		+ SQLiteHelper.COLUMN_NAME_TIME +" between "+ (time - (1000*60*2)) +" and "+ (time + (1000*60*2)), null);
		
			
//		cursor = database.rawQuery("select max( "+ SQLiteHelper.COLUMN_NAME_TIME +" ) as maxTime from "
//				+ SQLiteHelper.TABLE_NAME + " group by " + SQLiteHelper.COLUMN_NAME_TIME, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			maxTime	 =  cursor.getLong(3);
			Log.i("SentencesDAO", "&&&&& " + maxTime);
			count++;
			cursor.moveToNext();
		}

		if (count > 0){
			maxTime = maxTime + (1000*60*3);
			time = maxTime;
			Log.i("SentencesDAO", "&&2222&&& " + time);
			cursor.close();
			time = getCheckTimeConflict(time);
		}
		
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		return time;
		
	}

	private Sentences cursorToSentences(Cursor cursor) {
		Sentences sentences = new Sentences();
		sentences.setId(cursor.getLong(0));
		sentences.setWorld(cursor.getString(1));
		sentences.setWorldTrans(cursor.getString(2));
		sentences.setTime(cursor.getLong(3));
		return sentences;
	}
}
