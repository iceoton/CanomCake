package com.iceoton.canomcake.model;

import com.google.gson.annotations.Expose;

/**
 * Created by jaturon on 2/21/2016 AD.
 */

public class Category {
    @Expose
    int id;
    @Expose
    String name_th;
    @Expose
    String image;

    public int getId() {
        return id;
    }

    public String getNameThai() {
        return name_th;
    }

    public String getImageUrl() {
        return image;
    }
}
