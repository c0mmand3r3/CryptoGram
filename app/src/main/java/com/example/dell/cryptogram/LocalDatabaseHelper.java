package com.example.dell.cryptogram;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocalDatabaseHelper extends SQLiteOpenHelper {
    public static final String database_name="Local__DBMS_CHITCHAT_";
    public static final String table_name="login_remember_password";
    public static final String col_1="ID";
    public static final String col_2="EMAIL";
    public static final String col_3="PASSWORD";
    public LocalDatabaseHelper(Context context) {
        super(context, database_name, null, 1);
        SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+table_name+"(" +
                col_1+" integer primary key autoincrement," +
                col_2+" text," +
                col_3+" text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+table_name);
        onCreate(db);
    }

    public void insert_values(String email,String password){
        ContentValues contentValues=new ContentValues();
        contentValues.put(col_2,email);
        contentValues.put(col_3,password);
        this.getWritableDatabase().insertOrThrow(table_name,"",contentValues);
    }

    public void list_all_values(){
        Cursor cursor=this.getReadableDatabase().rawQuery("select * from "+table_name,null);
        while(cursor.moveToNext()){
            Log.i("Database Values are : ",col_1+" : "+cursor.getInt(0)+"\t"+col_2+" : "+cursor.getString(1)+"\t" +
                    col_3+" : "+cursor.getString(2)+"");
        }
    }
    public String[] get_remember_login(){
        String[] values=new String[2];
        Cursor cursor=this.getReadableDatabase().rawQuery("select * from "+table_name,null);
        if(cursor.moveToNext()){
            values[0]=cursor.getString(1);
            values[1]=cursor.getString(2);
        }else{
            values=null;
        }
        return values;
    }
    public void delete_Table(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from "+table_name);
        sqLiteDatabase.close();
    }
}
