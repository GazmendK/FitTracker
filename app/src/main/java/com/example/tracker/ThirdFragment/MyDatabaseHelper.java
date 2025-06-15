package com.example.tracker.ThirdFragment;

import androidx.annotation.Nullable;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private String DATABASE_NAME;
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "tableData";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ROW = "row_index";
    public static final String COLUMN_COL = "col";
    public static final String COLUMN_VALUE = "value";
    public static final String COLUMN_NAME = "name";


    public MyDatabaseHelper(@Nullable Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
        this.DATABASE_NAME = databaseName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ROW + " INTEGER, " +
                COLUMN_COL + " INTEGER, " +
                COLUMN_VALUE + " TEXT)";
               // COLUMN_NAME + "TEXT)";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


}
