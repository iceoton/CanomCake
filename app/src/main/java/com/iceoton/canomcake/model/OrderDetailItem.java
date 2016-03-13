package com.iceoton.canomcake.model;

import com.google.gson.annotations.Expose;

public class OrderDetailItem {
    @Expose
    String product_code;
    @Expose
    float price;
    @Expose
    String name_th;
    @Expose
    String name_en;
    @Expose
    int amount;
    @Expose
    float total_price;
    @Expose
    String image;

    public String getProductCode() {
        return product_code;
    }

    public float getPrice() {
        return price;
    }

    public String getNameThai() {
        return name_th;
    }

    public String getNameEng() {
        return name_en;
    }

    public int getAmount() {
        return amount;
    }

    public float getTotalPrice() {
        return total_price;
    }

    public String getImageUrl() {
        return image;
    }
}
