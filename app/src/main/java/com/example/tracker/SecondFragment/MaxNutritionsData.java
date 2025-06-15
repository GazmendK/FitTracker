package com.example.tracker.SecondFragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MaxNutritionsData extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "maxNutrition.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "maxNutritions";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_KCAL = "calories";
    public static final String COLUMN_CARBOHYDRATE = "carbohydrates";
    public static final String COLUMN_PROTEIN = "proteins";
    public static final String COLUMN_FAT = "fats";



    public MaxNutritionsData(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_KCAL +         " REAL, " +
                COLUMN_CARBOHYDRATE + " REAL, " +
                COLUMN_PROTEIN+       " REAL, " +
                COLUMN_FAT +          " REAL)";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertValue( double kcal, double carbs, double protein, double fat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_KCAL, kcal);
        values.put(COLUMN_CARBOHYDRATE, carbs);
        values.put(COLUMN_PROTEIN, protein);
        values.put(COLUMN_FAT, fat);

        db.insert(TABLE_NAME, null, values);
    }

    public ArrayList<Integer> loadNutritionData() {
        ArrayList<Integer> nutritionData = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = "_id = ?";
        String[] selectionArgs = {"1"};

        // Cursor für die Abfrage erstellen
        Cursor cursor = db.query(
                TABLE_NAME,       // Tabellenname
                null,             // Alle Spalten abrufen
                selection,        // WHERE-Klausel
                selectionArgs,    // Argumente für WHERE-Klausel
                null,             // Gruppieren
                null,             // Filter
                null              // Sortierung
        );

        while (cursor.moveToNext()) {

            // Daten aus dem Cursor abrufen und der Zeile hinzufügen
            nutritionData.add(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KCAL)));
            nutritionData.add(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CARBOHYDRATE)));
            nutritionData.add(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PROTEIN)));
            nutritionData.add(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FAT)));


            // Zeile zur Hauptliste hinzufügen
        }

        cursor.close();
        return nutritionData;
    }

    public void updateMaxNutrition(double kcal, double carbs, double protein, double fat) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_KCAL, kcal);
        cv.put(COLUMN_CARBOHYDRATE, carbs);
        cv.put(COLUMN_PROTEIN, protein);
        cv.put(COLUMN_FAT, fat);

        // Update ausführen
        SQLiteDatabase db = this.getWritableDatabase(); // Schreibender Zugriff auf die DB
        int rowsUpdated = db.update(
                TABLE_NAME,  // Tabellenname
                cv,                // Werte, die aktualisiert werden sollen
                "_id = 1",         // Bedingung (WHERE-Klausel)
                null // Platzhalterwert für _id
        );

        db.close(); // Datenbank schließen
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
