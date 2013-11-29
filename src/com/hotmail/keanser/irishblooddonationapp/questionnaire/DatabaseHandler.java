package com.hotmail.keanser.irishblooddonationapp.questionnaire;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper  {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "questionnaireDB";

	// Questionnaires table name
	private static final String TABLE_QUESTIONNAIRE = "questionnaire";

	// Questionnaires Table Columns names
	private static final String KEY_QNUM = "qnum";
	private static final String KEY_QUEST = "question";
	private static final String KEY_RESP = "response";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_QUESTIONNAIRE + "("
				+ KEY_QNUM + " INTEGER PRIMARY KEY," + KEY_QUEST + " TEXT,"
				 + KEY_RESP + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONNAIRE);

		// Create tables again
		onCreate(db);


	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new questionnaire
	void addQuestionnaire(Questionnaire questionnaire) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_QUEST, questionnaire.getQuestion()); // Questionnaire question
		values.put(KEY_RESP, questionnaire.getResponse()); // Questionnaire response

		// Inserting Row
		db.insert(TABLE_QUESTIONNAIRE, null, values);
		db.close(); // Closing database connection
	}

	// Getting single questionnaire
	Questionnaire getQuestionnaire(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_QUESTIONNAIRE, new String[] { KEY_QNUM,
				KEY_QUEST, KEY_RESP }, KEY_QNUM + "=?",
				new String[] { String.valueOf(id) }, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Questionnaire questionnaire = new Questionnaire(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
		// return questionnaire
		return questionnaire;
	}

	// Getting All Questionnaires
	public List<Questionnaire> getAllQuestionnaires() {
		List<Questionnaire> questionnaireList = new ArrayList<Questionnaire>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_QUESTIONNAIRE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Questionnaire questionnaire = new Questionnaire();
				questionnaire.setQnum(Integer.parseInt(cursor.getString(0)));
				questionnaire.setQuestion(cursor.getString(1));
//				questionnaire.setAnswer(cursor.getString(2));
				questionnaire.setResponse(cursor.getString(2));
				// Adding questionnaire to list
				questionnaireList.add(questionnaire);
			} while (cursor.moveToNext());
		}

		// return questionnaire list
		return questionnaireList;
	}

	// Updating single questionnaire
	public int updateQuestionnaire(Questionnaire questionnaire) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_QUEST, questionnaire.getQuestion());
//		values.put(KEY_ANS, questionnaire.getAnswer());
		values.put(KEY_RESP, questionnaire.getResponse());

		// updating row
		return db.update(TABLE_QUESTIONNAIRE, values, KEY_QNUM + " = ?",
				new String[] { String.valueOf(questionnaire.getQnum()) });
	}

	// Deleting single questionnaire
	public void deleteQuestionnaire(Questionnaire questionnaire) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_QUESTIONNAIRE, KEY_QNUM + " = ?",
				new String[] { String.valueOf(questionnaire.getQnum()) });
		db.close();
	}


	// Getting questionnaires Count
	public int getQuestionnairesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_QUESTIONNAIRE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	// Getting All Questions
	public List<Questionnaire> getAllQuestions() {
		List<Questionnaire> questionList = new ArrayList<Questionnaire>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_QUESTIONNAIRE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Questionnaire questionnaire = new Questionnaire();
				questionnaire.setQuestion(cursor.getString(0));
				
				// Adding contact to list
				questionList.add(questionnaire);
			} while (cursor.moveToNext());
		}

		// return contact list
		return questionList;
	}

}
