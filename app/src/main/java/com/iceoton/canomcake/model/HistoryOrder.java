package com.iceoton.canomcake.model;

import com.google.gson.annotations.Expose;

public class HistoryOrder {
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
}
