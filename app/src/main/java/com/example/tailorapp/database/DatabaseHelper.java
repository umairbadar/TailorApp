package com.example.tailorapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "cart_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "Name";
    private static final String COL3 = "Price";
    private static final String COL4 = "Image";
    private static final String COL5 = "Fabric_details";
    private static final String COL6 = "Measurements";
    private static final String COL7 = "Pickup_date";
    private static final String COL8 = "Pickup_time";

    public DatabaseHelper(Context context){
        super(context, TABLE_NAME, null,1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + COL1 + " TEXT, " + COL2
                + " TEXT, " + COL3 + " TEXT, " + COL4 + " TEXT, "  + COL5 + " TEXT, " + COL6 + " TEXT, "
                + COL7 + " TEXT, " + COL8 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String id, String name, String price, String image, String fabric_details, String measurements, String pickupDate, String pickupTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, id);
        contentValues.put(COL2, name);
        contentValues.put(COL3, price);
        contentValues.put(COL4, image);
        contentValues.put(COL5, fabric_details);
        contentValues.put(COL6, measurements);
        contentValues.put(COL7, pickupDate);
        contentValues.put(COL8, pickupTime);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1){
            return  false;
        } else {
            return true;
        }
    }

    public Cursor getData(){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getCount(){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteItem(String id){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " +
                COL1 + " = '" + id + "'";

        db.execSQL(query);
    }

    /*public Cursor getItemID(){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + name + "'";

        Cursor data = db.rawQuery(query, null);
        return data;
    }*/
}
