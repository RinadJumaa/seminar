package edu.cs.sm.GroupLogIn_Register;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class GDBhelper extends SQLiteOpenHelper {

    public final String DBNAME = "logingroup.db";


    public GDBhelper(Context context) { //
        super(context, "logingroup.db", null, 1); // new database named login.db
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {

        MyDB.execSQL("create Table groups(" +
                "Name TEXT," +
                "group_id TEXT primary key," +
                "password TEXT)");//create a new table
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {

        MyDB.execSQL("drop Table if exists groups");
    }

    public Boolean insertData(String name,String group_id, String password){

        //insertion of data into the table
        SQLiteDatabase MyDB = this.getWritableDatabase(); //helps open the database for reading or writing
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name); //column called name
        contentValues.put("group_id", group_id); // column called id
        contentValues.put("password", password); // column called password

        long result = MyDB.insert("groups", null, contentValues); //insert a new group
        if (result == -1 ) return false;
        else return true;
    }

    /* this function basically checks if the user already exists in the db table*/
    public Boolean checkID(String ID){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from groups where group_id =? ",
                new String [] {ID});

        if (cursor.getCount() > 0) return true; //found a user
        else
            return false;
    }

    /* this function basically checks if the password of the id is correct*/
    public Boolean checkidPassword(String id, String password){

        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from groups where group_id = ? and password = ?",
                new String [] {id,password});
        if (cursor.getCount() > 0) return true; //found a user and password
        else
            return false;
    }
    /* this function basically prints all groups on console*/
    public Cursor getData(){

        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from groups", null);
        if (cursor.getCount() > 0){
            StringBuffer stringBuffer = new StringBuffer();
            while (cursor.moveToNext()){
                stringBuffer.append("Name: " + cursor.getString(0) + "\n");
                stringBuffer.append("id: " + cursor.getString(1) + "\n");
                stringBuffer.append("Password: " + cursor.getString(2) + "\n");
            }

            System.out.println(stringBuffer);

        }
        return cursor;

    }

//    public Cursor getUserName(String id){
//        SQLiteDatabase MyDB = this.getWritableDatabase();
//        Cursor cursor = MyDB.rawQuery("select * from groups where id = ?"
//                , new String [] { id});
//        return cursor;
//    }

}
