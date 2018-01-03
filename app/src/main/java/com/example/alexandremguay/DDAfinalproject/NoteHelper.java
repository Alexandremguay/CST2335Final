package com.example.alexandremguay.DDAfinalproject;

/**
 * Created by Danny on 2018-01-02.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class NoteHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME  = "note2.db";
    public static final int VERSION_NUM = 3;
    public static final String TABLE_NAME ="Notes2";
    public static final String KEY_ID = "_id";
    public static final String KEY_MESSAGE = "note2";



    public NoteHelper(Context ctx){

        super(ctx,DATABASE_NAME,null,VERSION_NUM);

    }

    public void onCreate(SQLiteDatabase db){

        String query = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_MESSAGE + " TEXT " + " );" ;

        db.execSQL(query);

    }

    public String getNote(Cursor c) {
        return(c.getString(1));
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase1, int i, int i1) {

        sqLiteDatabase1.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
        onCreate(sqLiteDatabase1);

    }


    public void update(String id, String note) {
        ContentValues cv = new ContentValues();
        String[] args = {id};

        cv.put("note", note);

        getWritableDatabase().update(TABLE_NAME, cv, "_id=?", args);

    }

    public void delete(String id) {

        getWritableDatabase().delete(TABLE_NAME, "_id=?", new String[]{id});
    }

}

