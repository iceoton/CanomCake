package com.iceoton.canomcake.model.response;

import com.google.gson.annotations.Expose;
import com.iceoton.canomcake.model.Product;

import java.util.ArrayList;

public class GetAllProductResponse {
    @Expose
    int success;
    @Expose
    int error;
    @Expose
    String error_msg;
    @Expose
    ArrayList<Product> result;

    public int getSuccess() {
        return success;
    }

    public int getError() {
        return error;
    }

    public String getErrorMsg() {
        return error_msg;
    }

    public ArrayList<Product> getResult() {
        return result;
    }
}
