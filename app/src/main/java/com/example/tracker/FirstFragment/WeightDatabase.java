package com.example.tracker.FirstFragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class WeightDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weightData.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "weightData";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_WEIGHT = "weight";



    public WeightDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE +         " TEXT, " +
                COLUMN_WEIGHT +       " REAL)";

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertValue(String date, double weight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_DATE, date);
        values.put(COLUMN_WEIGHT, weight);

        db.insert(TABLE_NAME, null, values);
    }

    public ArrayList<ArrayList<String>> loadWeightData() {
        ArrayList<ArrayList<String>> weightData = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            ArrayList<String> weight = new ArrayList<>();


            weight.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
            weight.add(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT))));


            weightData.add(weight);
        }

        cursor.close();
        return weightData;
    }

    public void clearTableData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_NAME, null, null);
            db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + TABLE_NAME + "';");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }


}
