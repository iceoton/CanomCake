package com.iceoton.canomcake.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseDAO {
    private static final String TAG = "DATABASE";
    private Context mContext;
    private SQLiteDatabase database;
    private DatabaseOpenHelper openHelper;

    public DatabaseDAO(Context mContext) {
        this.mContext = mContext;
        openHelper = new DatabaseOpenHelper(mContext);
    }

    public void open() throws SQLException {
        database = openHelper.getWritableDatabase();
    }

    public void close() {
        openHelper.close();
    }

    public ArrayList<OrderItem> getAllOrderItem() {
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM " + OrderItemTable.TABLE_NAME;
        Cursor cursor = database.rawQuery(sql, null);

        if (cursor.getCount() > 0) {
            OrderItem orderItem;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                orderItem = OrderItem.newInstance(cursor);
                orderItems.add(orderItem);
                cursor.moveToNext();
            }
        }
        cursor.close();

        Log.d(TAG, "Number of item in \"order item\": " + orderItems.size());

        return orderItems;
    }

    public void addOrderItem(OrderItem orderItem) {
        ContentValues values = orderItem.toContentValues();
        long insertIndex = database.insert(OrderItemTable.TABLE_NAME, null, values);

        if (insertIndex == -1) {
            Log.d(TAG, "An error occurred on inserting order item table.");
        } else {
            Log.d(TAG, "insert order item successful.");
        }
    }

    public void updateOrderItemByValues(int itemId, ContentValues values) {
        String[] whereArgs = {String.valueOf(itemId)};

        int affected = database.update(OrderItemTable.TABLE_NAME, values, "id=?", whereArgs);
        if (affected == 0) {
            Log.d(TAG, "[OrderItemTable]update orderitem id " + itemId
                    + " not successful.");
        }
    }

    public void deleteOrderItem(int itemId) {
        String whereClause = "id=?";
        String[] whereArgs = {String.valueOf(itemId)};
        database.delete(OrderItemTable.TABLE_NAME, whereClause, whereArgs);
    }

    public void clearOrderItem() {
        //Delete all row out of Pre-Order table
        database.delete(OrderItemTable.TABLE_NAME, null, null);
    }

    public int getNumberOfOrderItem(){
        int count = 0;
        String sql = "SELECT COUNT(*) FROM " + OrderItemTable.TABLE_NAME;
        Cursor cursor = database.rawQuery(sql, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            count = cursor.getInt(0);
        }
        cursor.close();

        Log.d(TAG, "The number of order items is " + count);

        return count;
    }


}
