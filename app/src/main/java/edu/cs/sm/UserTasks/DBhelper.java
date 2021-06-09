package edu.cs.sm.UserTasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBhelper extends SQLiteOpenHelper {

    public final String DBNAME = "login.db";


    public DBhelper(Context context) { //
        super(context, "login.db", null, 1); // new database named login.db
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {

        MyDB.execSQL("create Table users(Name TEXT,email TEXT primary key, password TEXT)");//create a new table
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {

        MyDB.execSQL("drop Table if exists users");
    }

    public Boolean insertData(String name,String email, String password){

        //insertion of data into the table
        SQLiteDatabase MyDB = this.getWritableDatabase(); //helps open the database for reading or writing
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name); //column called name
        contentValues.put("email", email); // column called email
        contentValues.put("password", password); // column called password

        long result = MyDB.insert("users", null, contentValues); //insert a new user
        if (result == -1 ) return false;
        else return true;
    }

    /* this function basically checks if the user already exists in the db table*/
    public Boolean checkemail(String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users where email =? ",
                new String [] {email});

        if (cursor.getCount() > 0) return true; //found a user
        else
            return false;
    }

    /* this function basically checks if the password of the email is correct*/
    public Boolean checkemailPassword(String email, String password){

        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users where email = ? and password = ?",
                new String [] {email,password});
        if (cursor.getCount() > 0) return true; //found a user and password
        else
            return false;
    }
    /* this function basically prints all users on console*/
    public Cursor getData(){

        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users", null);
        if (cursor.getCount() > 0){
            StringBuffer stringBuffer = new StringBuffer();
            while (cursor.moveToNext()){
                stringBuffer.append("Name: " + cursor.getString(0) + "\n");
                stringBuffer.append("Email: " + cursor.getString(1) + "\n");
                stringBuffer.append("Password: " + cursor.getString(2) + "\n");
            }

            System.out.println(stringBuffer);

        }
        return cursor;

    }

    public Cursor getUserName(String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users where email = ?"
                , new String [] { email});
        return cursor;
    }

}
