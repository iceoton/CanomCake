package com.iceoton.canomcake.database;

import android.content.ContentValues;
import android.database.Cursor;

public class OrderItem {
    int id;
    String productCode;
    int amount;

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(OrderItemTable.Columns._PRODUCT_CODE, productCode);
        values.put(OrderItemTable.Columns._AMOUNT, amount);
        return values;
    }

    public static OrderItem newInstance(Cursor cursor) {
        OrderItem orderItem = new OrderItem();
        orderItem.fromCursor(cursor);
        return orderItem;
    }

    public void fromCursor(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndexOrThrow(OrderItemTable.Columns._ID));
        this.productCode = cursor.getString(
                cursor.getColumnIndexOrThrow(OrderItemTable.Columns._PRODUCT_CODE));
        this.amount = cursor.getInt(cursor.getColumnIndexOrThrow(OrderItemTable.Columns._AMOUNT));
    }

    public int getId() {
        return id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
