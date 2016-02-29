package com.iceoton.canomcake.database;

import android.database.sqlite.SQLiteDatabase;

public class OrderItemTable {
    public static final String TABLE_NAME = "order_item";

    public static class Columns {
        public Columns() {
        }
        public static final String _ID = "id";
        public static final String _PRODUCT_CODE = "product_code";
        public static final String _AMOUNT = "amount";
    }

    public static void creatTable(SQLiteDatabase database){
        // Database creation sql statement
        final String DATABASE_CREATE = "create table "
                + TABLE_NAME + "(" + Columns._ID + " INTEGER primary key autoincrement, "
                + Columns._PRODUCT_CODE + " TEXT NOT NULL, "
                + Columns._AMOUNT +" INTEGER NOT NULL);";

        database.execSQL(DATABASE_CREATE);
    }
}
