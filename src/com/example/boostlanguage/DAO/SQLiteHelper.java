package com.example.boostlanguage.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_NAME = "sentences";
  public static final String COLUMN_NAME_ID = "id";
  public static final String COLUMN_NAME_MAIN_SEN = "world";
  public static final String COLUMN_NAME_TRANS = "worldTrans";

  public static final String TABLE_NAME_setting = "setting";
  public static final String COLUMN_NAME_ID_SETTING = "id";
  public static final String correct_answer = "correctAnswer";
  public static final String wrong_answer = "wrong_answer";
  
  private static final String DATABASE_NAME = "sentences.db";
  private static final int DATABASE_VERSION = 6;

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_NAME + "(" + COLUMN_NAME_ID
      + " integer primary key autoincrement, " + COLUMN_NAME_MAIN_SEN
      + " text not null, " + COLUMN_NAME_TRANS 
      + ");";

  private static final String DATABASE_CREATE_setting = "create table "
	      + TABLE_NAME_setting + "(" + COLUMN_NAME_ID_SETTING
	      + " integer primary key autoincrement, " + correct_answer
	      + " text not null, " + wrong_answer
	      + ");";

  
  public SQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
	  Log.i(SQLiteHelper.class.getName(), ">>>>>>>>>>> onCreate >>>");
	  database.execSQL(DATABASE_CREATE);
	  database.execSQL(DATABASE_CREATE_setting);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(SQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_setting);
    onCreate(db);
  }

} 