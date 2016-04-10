package com.iceoton.canomcake.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import com.iceoton.canomcake.database.DatabaseDAO;
import com.iceoton.canomcake.database.OrderItem;
import com.iceoton.canomcake.model.Product;

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

    public boolean addProductToCart(Product product){
        boolean isSuccess = false;
        OrderItem orderItem = new OrderItem();
        orderItem.setProductCode(product.getCode());
        orderItem.setAmount(1);
        DatabaseDAO databaseDAO = new DatabaseDAO(mContext);
        databaseDAO.open();

        if((databaseDAO.getAmountOfOrderItem(orderItem) + 1) <= product.getAvailable()){
            databaseDAO.addOrderItem(orderItem);
            isSuccess = true;
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
            alertDialog.setTitle("สินค้ามีจำนวนจำกัด");
            alertDialog.setMessage("สินค้ามีจำนวน " + product.getAvailable() + " " + product.getUnit());
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "ตกลง", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
        }
        databaseDAO.close();

        return isSuccess;
    }
}
