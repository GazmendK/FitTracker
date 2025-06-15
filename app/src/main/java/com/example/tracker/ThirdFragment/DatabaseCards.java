package com.example.tracker.ThirdFragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseCards extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cards.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "tableData";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EXERCISES = "exercises";
    public static final String COLUMN_DAYS = "days";


    public DatabaseCards(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EXERCISES + " INTEGER, " +
                COLUMN_DAYS + " INTEGER)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertValue(String name, int exercises, int days) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EXERCISES, exercises);
        values.put(COLUMN_DAYS, days);

        db.insert(TABLE_NAME, null, values);
    }


    public int addTrainingPlan(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);

        // Füge den Eintrag ein und erhalte die generierte ID
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return (int) id;
    }


    public ArrayList<ArrayList<String>> loadTableData() {
        ArrayList<ArrayList<String>> trainingPlans = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            ArrayList<String> plan = new ArrayList<>();

            plan.add(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ID))));
            plan.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            plan.add(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_EXERCISES))));
            plan.add(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DAYS))));

            trainingPlans.add(plan);
        }

        db.close();
        return trainingPlans;
    }


    public boolean updateExercises(String name, int exercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXERCISES, exercise);

        int rowsUpdated = db.update(TABLE_NAME, values, COLUMN_NAME + " = ?", new String[]{name});
        db.close();
        return rowsUpdated > 0;
    }

    public boolean updateDays(String name, int days) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DAYS, days);

        int rowsUpdated = db.update(TABLE_NAME, values, COLUMN_NAME + " = ?", new String[]{name});
        db.close();
        return rowsUpdated > 0;
    }


    public void deleteTrainingPlan(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_NAME + " = ?", new String[]{name});
        //reorderIds();
        db.close();
        //return rowsDeleted > 0;
    }
    public void reorderIds() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Erstelle eine temporäre Tabelle
        db.execSQL("CREATE TABLE temp_table AS SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID);

        // Lösche die alte Tabelle
        db.execSQL("DROP TABLE " + TABLE_NAME);

        // Benenne die temporäre Tabelle in die originale Tabelle um
        db.execSQL("ALTER TABLE temp_table RENAME TO " + TABLE_NAME);

        // Optional: Setze den Autoincrement-Wert zurück
        db.execSQL("DELETE FROM sqlite_sequence WHERE name = '" + TABLE_NAME + "';");

        db.close();
    }

    public void clearTableData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(MyDatabaseHelper.TABLE_NAME, null, null);
            db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + MyDatabaseHelper.TABLE_NAME + "';");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
