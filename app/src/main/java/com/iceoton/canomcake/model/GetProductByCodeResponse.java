package com.iceoton.canomcake.model;

import com.google.gson.annotations.Expose;

public class GetProductByCodeResponse {
    @Expose
    int success;
    @Expose
    int error;
    @Expose
    String error_msg;
    @Expose
    Product result;

    public int getSuccess() {
        return success;
    }

    public int getError() {
        return error;
    }

    public String getErrorMsg() {
        return error_msg;
    }

    public Product getResult() {
        return result;
    }
}
