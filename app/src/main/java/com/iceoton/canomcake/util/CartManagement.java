package com.iceoton.canomcake.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.iceoton.canomcake.database.DatabaseDAO;

public class CartManagement {
    Context mContext;

    public CartManagement(Context mContext) {
        this.mContext = mContext;
    }

    public void loadCountInto(TextView viewToShowCount){
        DatabaseDAO databaseDAO = new DatabaseDAO(mContext);
        databaseDAO.open();
        int count =  databaseDAO.getNumberOfOrderItem();
        if(count > 0) {
            viewToShowCount.setVisibility(View.VISIBLE);
            viewToShowCount.setText(String.valueOf(count));
        } else {
            viewToShowCount.setVisibility(View.INVISIBLE);
        }
        databaseDAO.close();
    }
}
