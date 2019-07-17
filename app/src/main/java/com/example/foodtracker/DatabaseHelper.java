package com.example.foodtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "employeeDetails.db";
    public static final String TABLE_NAME = "employeeTable";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "Name";
    public static final String COL_3 = "Weight";
    public static final String COL_4 = "Price";
    public static final String COL_5 = "Description";
    public static final String COL_6 = "Availability";

    String available = "Available";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    /**
     * Create a table
     * @param db Table name is " employeeDetails "
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME +" (" +
                " ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " Name TEXT," +
                " Weight TEXT," +
                " Price INTEGER," +
                " Description TEXT," +
                " Availability TEXT DEFAULT 'Avalible') ");

    }

    /**
     * When ever change the data base version this method will drop the table Nd create an new table
     * with same name
     * @param db
     * @param oldVersion -> old version
     * @param newVersion -> new version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);

    }

    /**
     *
     * @param db
     * @param oldVersion -> old version
     * @param newVersion -> new version
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    /**
     * Insert user input to the database
     * @param name Ingredient name
     * @param weight Ingredient weight
     * @param price Ingredient price
     * @param desc Ingredient Description
     * @return
     */
    public boolean insertData (String name, int weight, int price, String desc, String availability) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, name);
        values.put(COL_3, weight);
        values.put(COL_4, price);
        values.put(COL_5, desc);
        values.put(COL_6, availability);

        long results = db.insert(TABLE_NAME, null, values);

        // ---- If the data is not inserted its return -1
        if (results == -1)
            return false;
        else
            return true;

    }

    /**
     * Dis[play all data in the database
     * @return data
     */
    public Cursor viewData(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor dbData = db.rawQuery("SELECT * FROM "+ TABLE_NAME +" ORDER BY "+ COL_2+" ASC " ,null);
        return dbData;

    }

    /**
     * Update database
     * @param name Ingredient name
     * @param weight Ingredient weight
     * @param price Ingredient price
     * @param desc Ingredient Description
     * @return Database Data
     */
    public boolean updateData (String  id,String name, int weight, int price, String desc, String availability) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1, id);
        values.put(COL_2, name);
        values.put(COL_3, weight);
        values.put(COL_4, price);
        values.put(COL_5, desc);
        values.put(COL_6, availability);

        db.update(TABLE_NAME, values, "ID = ?", new String[] {id});
        return true;
    }

    /**
     * This method is only update the availability of the ingredients that user selected
     * @param name
     * @param availability
     * @return
     */
    public boolean updateAvailability(String name, String availability){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, name);
        values.put(COL_6, availability);

        System.out.println("============== Just Updated ============");
        System.out.println("Name ----> "+ name);
        System.out.println("Availability ----> "+ availability);

        db.update(TABLE_NAME, values, "Name = ?", new String[] {name});
        return true;
    }

    /**
     * This method only display where availability is "Available"
     * @return Available data
     */
    public Cursor viewSelectedData(String name){

        //SQLiteDatabase db = this.getWritableDatabase();
//        Cursor dbData = db.rawQuery(
//                "SELECT * FROM "+ TABLE_NAME + " WHERE "+ COL_2 + "= name ", null);
//        return dbData;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor dbData = db.rawQuery(
                "SELECT * FROM "+ TABLE_NAME +" WHERE "
                        + COL_2 + " LIKE '%"+name+"%'" , null);
        return dbData;

    }

    /**
     * This method only display where availability is "Available"
     * @return Available data
     */
    public Cursor viewAvailableData(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor dbData = db.rawQuery(
                "SELECT * FROM "+ TABLE_NAME + " ORDER BY "+ COL_2+" ASC", null);
        return dbData;

    }

    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }
}
