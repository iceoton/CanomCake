package com.iceoton.canomcake.model.response;

import com.google.gson.annotations.Expose;

public class ForgetPasswordResponse {
    @Expose
    int success;
    @Expose
    int error;
    @Expose
    String error_msg;
    @Expose
    String result;

    public int getSuccessValue() {
        return success;
    }

    public int getErrorValue() {
        return error;
    }

    public String getErrorMessage() {
        return error_msg;
    }

    public String getResult() {
        return result;
    }
}
