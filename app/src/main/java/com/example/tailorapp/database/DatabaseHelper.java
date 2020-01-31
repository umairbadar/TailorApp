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
    private static final String COL9 = "image_status";
    private static final String COL10 = "amount";
    private static final String COL11 = "fabric_id";
    private static final String COL12 = "measurement_id";


    public DatabaseHelper(Context context){
        super(context, TABLE_NAME, null,1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + COL1 + " TEXT, " + COL2
                + " TEXT, " + COL3 + " TEXT, " + COL4 + " BLOB, "  + COL5 + " TEXT, " + COL6 + " TEXT, "
                + COL7 + " TEXT, " + COL8 + " TEXT, " + COL9 + " TEXT, " + COL10 + " REAL, " + COL11 + " TEXT, " + COL12 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String id, String name, String price, byte[] image, String fabric_details, String measurements, String pickupDate, String pickupTime, String image_status, int amount, String fabric_id, String measurement_id){
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
        contentValues.put(COL9, image_status);
        contentValues.put(COL10, amount);
        contentValues.put(COL11, fabric_id);
        contentValues.put(COL12, measurement_id);

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

    public void delete(){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME;
        db.execSQL(query);
    }

    public int sum(){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT SUM(" + COL10 + ") FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){

            return cursor.getInt(0);
        }
        else{

            return  -1;
        }
        //cursor.close();
    }
}
