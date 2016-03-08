package com.iceoton.canomcake.model;

import com.google.gson.annotations.Expose;

public class MakeOrderResponse {
    @Expose
    int success;
    @Expose
    int error;
    @Expose
    String error_msg;

    public int getSuccessValue() {
        return success;
    }

    public int getErrorValue() {
        return error;
    }

    public String getErrorMessage() {
        return error_msg;
    }
}
