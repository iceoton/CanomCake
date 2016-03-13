package com.iceoton.canomcake.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class OrderDetail {
    @Expose
    int id;
    @Expose
    int customer_id;
    @Expose
    double total_price;
    @Expose
    String status;
    @Expose
    String order_time;
    @Expose
    ArrayList<OrderDetailItem> detail;

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customer_id;
    }

    public double getTotalPrice() {
        return total_price;
    }

    public String getStatus() {
        return status;
    }

    public String getOrderTime() {
        return order_time;
    }

    public ArrayList<OrderDetailItem> getOrderDetailItem() {
        return detail;
    }
}
