package com.iceoton.canomcake.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by jaturon on 2/21/2016 AD.
 */
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
