package com.example.sos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CONTACT.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "contact_table";
    private static final String ID = "ID";
    private static final String NAME_COLUMN = "NAME";
    private static final String MOBILE_COLUMN = "MOBILE";


    DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +
                "("+ ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME_COLUMN + " TEXT," +
                MOBILE_COLUMN + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean insertDataFunc (String name, String mob)
    {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(NAME_COLUMN, name);
            contentValues.put(MOBILE_COLUMN, mob);

            long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public int count(){
        int count=0;
        String query="SELECT COUNT(*) FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        if(c.getCount()>0){
            c.moveToFirst();
            count=c.getInt(0);
        }
        c.close();
        return count;
    }

    public ArrayList<ContactModel> fetchData(){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery(" select * from "+TABLE_NAME,null);
        ArrayList<ContactModel> dataArrayList = new ArrayList<>();
        while (result.moveToNext()){
            ContactModel model = new ContactModel();
            model.id = result.getString(0);
            model.name = result.getString(1);
            model.number = result.getString(2);
            dataArrayList.add(model);
        }
        return dataArrayList;
    }

    public boolean updateData(String id, String name, String mob)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ID,id);
        contentValues.put(NAME_COLUMN, name);
        contentValues.put(MOBILE_COLUMN, mob);

        int result = db.update(TABLE_NAME, contentValues, "ID = ?",new String[]{id});
        if (result==-1)
            return false;
        else
            return true;
    }

    public boolean deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, " ID = ?",new String[]{id});
        if (result==-1)
            return false;
        else
            return true;
    }
}
