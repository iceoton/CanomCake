package com.iceoton.canomcake.model.response;

import com.google.gson.annotations.Expose;
import com.iceoton.canomcake.model.User;


public class UserLoginResponse {
    @Expose
    int success;
    @Expose
    int error;
    @Expose
    String error_msg;
    @Expose
    User result;

    public int getSuccessValue() {
        return success;
    }

    public int getErrorValue() {
        return error;
    }

    public String getErrorMessage() {
        return error_msg;
    }

    public User getUser() {
        return result;
    }
}
