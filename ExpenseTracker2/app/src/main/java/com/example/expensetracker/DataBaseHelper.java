package com.example.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final int version = 1;
    public static final String dbName = "Expenses.db";
    public static final String TABLE_NAME = "Expense";

    // Table Structure
    public static final String COL1 = "id";
    public static final String COL2 = "name";
    public static final String COL3 = "price";
    public static final String COL4 = "date";
    public static final String COL5 = "category";

    // Create Querry
    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( " + COL1 + " INTEGER PRIMARY KEY  AUTOINCREMENT, " + COL2 + " TEXT NOT NULL," + COL3 + " TEXT, " + COL4 + " TEXT, " + COL5 + " TEXT); ";
    // Drop Table
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DataBaseHelper(@Nullable Context context) {
        super(context, dbName, null, version);
    }

    // Creating Database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }
    // Action to perform on database update
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    // Insert data into database
    public boolean InsertProgram(ExpenseInfo info) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2,info.getName());
        cv.put(COL3,info.getPrice());
        cv.put(COL4,info.getDate());
        cv.put(COL5,info.getCategory());
        long result = db.insert(TABLE_NAME,null,cv);
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // View data from database
    public Cursor ViewData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("select * from " + TABLE_NAME, null);

        //Move cursor to first if not null
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return  cursor;
    }

    // Delete from Database
    public void delete(int ids) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "id=?";
        String[] whereArgs = new String[] { String.valueOf(ids) };
        db.delete(TABLE_NAME, whereClause, whereArgs);
    }

    public Cursor ViewSearch(String expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;

        cursor = db.rawQuery("SELECT * FROM Expense WHERE TRIM(category) = '"+expense.trim()+"'", null);
        //Move cursor to first if not null
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
}
