package com.example.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME="todo_list_";
    private static final String COL1="ID";
    private static final String COL2="NAME";
    private static final String COL3="DATE";
    private static final String COL4="TIME";
    private static final String COL5="CHECKBOX";



    public DatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME,null,3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable=" CREATE TABLE "+ TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT, " + COL3 + " TEXT, " + COL4 + " TEXT, " + COL5 + " INTEGER DEFAULT 0) ";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(todoEntry entry){
        SQLiteDatabase db1=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL2,entry.getName());
        contentValues.put(COL3,entry.getDate());
        contentValues.put(COL4,entry.getTime());
        long result=db1.insert(TABLE_NAME,null,contentValues);

        if(result==-1)
            return false;
        else
            return true;
    }

    public Cursor getData(){
        SQLiteDatabase db1=this.getWritableDatabase();
        String query=" SELECT * FROM "+TABLE_NAME;
        Cursor data=db1.rawQuery(query,null);
        return data;
    }

    public void check(int id,int check){
        SQLiteDatabase db1=this.getWritableDatabase();
        int val=1;
        ContentValues args = new ContentValues();
        if(check==1)
            val=0;
        args.put(COL5, val);
        db1.update(TABLE_NAME, args, COL1 + "=" + id, null);
    }

    public void delete(int id){
        SQLiteDatabase db1=this.getWritableDatabase();
        String query=" DELETE FROM " + TABLE_NAME + " WHERE ID = " + id;
        db1.execSQL(query);
    }

}