package com.user.aditya.gradbud;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Aditya on 1/23/2018.
 */

public class DB_Controller extends SQLiteOpenHelper {
        public DB_Controller(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
        {
            super(context,"Local.DB",factory,version);
        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase){
            sqLiteDatabase.execSQL("CREATE TABLE LOGIN(STATUS int(1),PIN varchar(4),NAME varchar(50),PERSON varchar(1),ID varchar(30),PASSWORD varchar(30));");
        }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS LOGIN;");
        onCreate(sqLiteDatabase);
    }

    public boolean check_status()
    {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM LOGIN",null);
        if(cursor.getCount()==1)
            return true;
        return false;
    }

    public boolean check_PIN(String PIN,int STAT)
    {
        Cursor cursor=this.getReadableDatabase().rawQuery("SELECT * FROM LOGIN WHERE PIN='"+PIN+"' AND PERSON='"+STAT+"'",null);
        System.out.println("PIN::"+PIN+"ROW COUNT::"+cursor.getCount());
        if(cursor.getCount()==1)
            return true;
        return false;
    }
    public boolean check_password(String Password)
    {
        Cursor cursor=this.getReadableDatabase().rawQuery("SELECT * FROM LOGIN WHERE PASSWORD='"+Password+"'",null);
        System.out.println("PIN::"+Password+"ROW COUNT::"+cursor.getCount());
        if(cursor.getCount()==1)
            return true;
        return false;
    }
    public void enter_login(String PIN,String NAME,int STAT,String ID,String PASSWORD)
    {
        System.out.print("INSERT INTO LOGIN VALUES(1,'"+PIN+"','"+NAME+"','"+String.valueOf(STAT)+"','"+ID+"','"+PASSWORD+"');");
        this.getWritableDatabase().execSQL("INSERT INTO LOGIN VALUES(1,'"+PIN+"','"+NAME+"','"+String.valueOf(STAT)+"','"+ID+"','"+PASSWORD+"');");
    }
    public void update_password(String password)
    {
        this.getWritableDatabase().execSQL("UPDATE LOGIN SET PASSWORD='"+password+"';");
    }
    public void update_pin(String PIN)
    {
        this.getWritableDatabase().execSQL("UPDATE LOGIN SET PIN='"+PIN+"';");
    }
    public void delete_login()
    {
        this.getWritableDatabase().execSQL("DELETE FROM LOGIN;");
    }
    public String[] selectall()
    {
        Cursor cursor=this.getReadableDatabase().rawQuery("SELECT * FROM LOGIN",null);
        //System.out.println("ROW COUNT::"+cursor.getCount());
        String result[]=new String[5];
        while(cursor.moveToNext())
        {
            result[0]=cursor.getString(1);
            result[1]=cursor.getString(2);
            result[2]=cursor.getString(3);
            result[3]=cursor.getString(4);
            result[4]=cursor.getString(5);
        }
        return result;
    }
}
