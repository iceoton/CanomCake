package com.iceoton.canomcake.database;

import android.content.ContentValues;
import android.database.Cursor;

public class NotificationItem {
    int id;
    String orderId;
    String message;

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(NotificationTable.Columns._ORDER_ID, orderId);
        values.put(NotificationTable.Columns._MESSAGE, message);
        return values;
    }

    public static NotificationItem newInstance(Cursor cursor) {
        NotificationItem item = new NotificationItem();
        item.fromCursor(cursor);
        return item;
    }

    public void fromCursor(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndexOrThrow(NotificationTable.Columns._ID));
        this.orderId = cursor.getString(
                cursor.getColumnIndexOrThrow(NotificationTable.Columns._ORDER_ID));
        this.message = cursor.getString(
                cursor.getColumnIndexOrThrow(NotificationTable.Columns._MESSAGE));
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
