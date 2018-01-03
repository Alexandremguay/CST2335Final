package com.example.alexandremguay.DDAfinalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alexandre M Guay on 2017-12-28.
 */

public class ListDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Database";
    private static int VERSION_NUM = 203;
    public static final String i = "KEY_ID";
    public static final String m = "KEY_TASK";
    public static final String c = "TASKS";


    public ListDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + c + "(" + i + " INTEGER PRIMARY KEY AUTOINCREMENT, " + m + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + c);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {

    }



}