package com.iceoton.canomcake.model;

import com.google.gson.annotations.Expose;

public class Product {
    @Expose
    String code;
    @Expose
    String category_id;
    @Expose
    String name_th;
    @Expose
    String name_en;
    @Expose
    String detail;
    @Expose
    String price;
    @Expose
    String unit;
    @Expose
    String image;

    public String getCode() {
        return code;
    }

    public int getCategoryId() {
        return Integer.parseInt(category_id);
    }

    public String getNameThai() {
        return name_th;
    }

    public String getNameEnglish() {
        return name_en;
    }

    public String getDetail() {
        return detail;
    }

    public double getPrice() {
        return Double.parseDouble(price);
    }

    public String getUnit() {
        return unit;
    }

    public String getImageUrl() {
        return image;
    }
}
