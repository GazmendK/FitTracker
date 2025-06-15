package com.example.tracker.ThirdFragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.tracker.ThirdFragment.MyDatabaseHelper;

import java.util.ArrayList;

public class DataRepository {
    private final MyDatabaseHelper dbHelper;

    public DataRepository(Context context,String databaseName) {
        dbHelper = new MyDatabaseHelper(context, databaseName);
    }

    // Methode zum Einfügen eines Werts
    public void insertValue(int row, int col, String value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyDatabaseHelper.COLUMN_ROW, row);
        values.put(MyDatabaseHelper.COLUMN_COL, col);
        values.put(MyDatabaseHelper.COLUMN_VALUE, value);
        db.insert(MyDatabaseHelper.TABLE_NAME, null, values);
    }

    // Methode zum Laden aller Daten
    public ArrayList<ArrayList<String>> loadTableData() {
        ArrayList<ArrayList<String>> tableData = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(MyDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int row = cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_ROW));
            int col = cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_COL));
            String value = cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_VALUE));

            // Logik zum Hinzufügen der Daten an die richtige Position in tableData
            while (tableData.size() <= row) tableData.add(new ArrayList<>());
            while (tableData.get(row).size() <= col) tableData.get(row).add("");


            tableData.get(row).set(col, value);

        }

        cursor.close();
        return tableData;
    }

    public int getMaxRow() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(" + MyDatabaseHelper.COLUMN_ROW + ") FROM " + MyDatabaseHelper.TABLE_NAME, null);
        int maxRow = cursor.moveToFirst() ? cursor.getInt(0) : 0;
        cursor.close();

//        if(maxRow < 2){
//            maxRow = 2;
//        }

        return maxRow;
    }

    public int getMaxColumn() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(" + MyDatabaseHelper.COLUMN_COL + ") FROM " + MyDatabaseHelper.TABLE_NAME, null);
        int maxCol = cursor.moveToFirst() ? cursor.getInt(0) : 0;
        cursor.close();
        Log.d("DataRepository","Column count: " +maxCol);

//        if(maxCol < 3){
//            maxCol = 3;
//        }

        return maxCol;
    }

    public void clearTableData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
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
