package com.iceoton.canomcake.database;

import android.database.sqlite.SQLiteDatabase;

public class NotificationTable {
    public static final String TABLE_NAME = "notification";
    public static class Columns {
        public Columns() {
        }
        public static final String _ID = "id";
        public static final String _ORDER_ID = "order_id";
        public static final String _MESSAGE = "message";
    }

    public static void creatTable(SQLiteDatabase database){
        // Database creation sql statement
        final String DATABASE_CREATE = "create table "
                + TABLE_NAME + "(" + Columns._ID + " INTEGER primary key autoincrement, "
                + Columns._ORDER_ID + " TEXT NOT NULL, "
                + Columns._MESSAGE +" TEXT NOT NULL);";

        database.execSQL(DATABASE_CREATE);
    }
}
