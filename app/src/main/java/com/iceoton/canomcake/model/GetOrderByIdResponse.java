package com.iceoton.canomcake.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class GetOrderByIdResponse {
    @Expose
    int success;
    @Expose
    int error;
    @Expose
    String error_msg;
    @Expose
    ArrayList<OrderDetail> result;

    public int getSuccessValue() {
        return success;
    }

    public int getErrorValue() {
        return error;
    }

    public String getErrorMessage() {
        return error_msg;
    }

    public ArrayList<OrderDetail> getResult() {
        return result;
    }
}
