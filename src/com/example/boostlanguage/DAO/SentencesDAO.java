package com.example.boostlanguage.DAO;

import java.util.ArrayList;
import java.util.List;

import com.example.boostlanguage.entity.Sentences;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SentencesDAO {

	// Database fields
	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;
	private String[] allColumns = { SQLiteHelper.COLUMN_NAME_ID,
			SQLiteHelper.COLUMN_NAME_MAIN_SEN, SQLiteHelper.COLUMN_NAME_TRANS };

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

	private Sentences cursorToSentences(Cursor cursor) {
		Sentences sentences = new Sentences();
		sentences.setId(cursor.getLong(0));
		sentences.setWorld(cursor.getString(1));
		sentences.setWorldTrans(cursor.getString(2));
		return sentences;
	}
}
