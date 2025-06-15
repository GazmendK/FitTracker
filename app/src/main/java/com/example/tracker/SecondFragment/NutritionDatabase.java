package com.example.tracker.SecondFragment;


import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.Nullable;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.tracker.ThirdFragment.MyDatabaseHelper;

import java.util.ArrayList;

public class NutritionDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "nutrition.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "nutrtion";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_KCAL = "calories";
    public static final String COLUMN_PROTEIN = "proteins";
    public static final String COLUMN_FAT = "fats";
    public static final String COLUMN_CARBOHYDRATE = "carbohydrates";
    public static final String COLUMN_SERVING = "serving";



    public NutritionDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE +         " TEXT, " +
                COLUMN_NAME +         " TEXT, " +
                COLUMN_KCAL +         " REAL, " +
                COLUMN_CARBOHYDRATE + " REAL, " +
                COLUMN_PROTEIN+       " REAL, " +
                COLUMN_FAT +          " REAL, " +
                COLUMN_SERVING +      " REAL)";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertValue(String date, String name, double kcal, double carbs, double protein, double fat, double serving) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_DATE, date);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_KCAL, kcal);
        values.put(COLUMN_CARBOHYDRATE, carbs);
        values.put(COLUMN_PROTEIN, protein);
        values.put(COLUMN_FAT, fat);
        values.put(COLUMN_SERVING, serving);

        db.insert(TABLE_NAME, null, values);
    }

    public ArrayList<ArrayList<String>> loadNutritionData() {
        ArrayList<ArrayList<String>> nutritionData = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            ArrayList<String> rowData = new ArrayList<>();

            // Daten aus dem Cursor abrufen und der Zeile hinzufügen
            rowData.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
            rowData.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            rowData.add(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_KCAL))));
            rowData.add(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CARBOHYDRATE))));
            rowData.add(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PROTEIN))));
            rowData.add(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FAT))));
            rowData.add(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SERVING))));


            // Zeile zur Hauptliste hinzufügen
            nutritionData.add(rowData);
        }

        cursor.close();
        return nutritionData;
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
