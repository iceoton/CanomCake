package com.iceoton.canomcake.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class GetAllCategoryResponse {
    @Expose
    int success;
    @Expose
    int error;
    @Expose
    String error_msg;
    @Expose
    ArrayList<Category> result;

    public int getSuccessValue() {
        return success;
    }

    public int getErrorValue() {
        return error;
    }

    public String getErrorMessage() {
        return error_msg;
    }

    public ArrayList<Category> getResult() {
        return result;
    }
}
