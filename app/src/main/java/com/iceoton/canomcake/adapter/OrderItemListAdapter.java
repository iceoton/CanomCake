package com.iceoton.canomcake.adapter;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iceoton.canomcake.R;
import com.iceoton.canomcake.database.DatabaseDAO;
import com.iceoton.canomcake.database.OrderItem;
import com.iceoton.canomcake.database.OrderItemTable;
import com.iceoton.canomcake.fragment.CartFragment;
import com.iceoton.canomcake.model.Product;

import java.util.ArrayList;

public class OrderItemListAdapter extends BaseAdapter {

    static class ViewHolder {
        public ImageView imageViewProduct;
        public TextView textProductName, textProductPrice, textDelete, textAmount;

        public ViewHolder(View convertView) {
            this.imageViewProduct = (ImageView) convertView.findViewById(R.id.image_product);
            this.textProductName = (TextView) convertView.findViewById(R.id.text_name);
            this.textProductPrice = (TextView) convertView.findViewById(R.id.text_price);
            this.textAmount = (TextView) convertView.findViewById(R.id.edit_amount);
            this.textDelete = (TextView) convertView.findViewById(R.id.text_delete);
        }
    }

    Context mContext;
    ArrayList<OrderItem> orderItems;
    ArrayList<Product> products;
    CartFragment cartFragment;

    public OrderItemListAdapter(Context mContext, ArrayList<OrderItem> orderItems,
                                ArrayList<Product> products, CartFragment cartFragment) {
        this.mContext = mContext;
        this.orderItems = orderItems;
        this.products = products;
        this.cartFragment = cartFragment;
    }

    @Override
    public int getCount() {
        return orderItems.size();
    }

    @Override
    public OrderItem getItem(int position) {
        return orderItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return orderItems.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_order_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Log.d("DEBUG", "get item " + position);
        final OrderItem orderItem = orderItems.get(position);
        // load product by product_code
        Product product = products.get(position);
        if (product != null) {
            Log.d("DEBUG", "load image " + product.getNameThai() + "finish");
            String imageUrl = mContext.getResources().getString(R.string.api_url)
                    + product.getImageUrl();
            Glide.with(mContext).load(imageUrl)
                    .placeholder(R.drawable.product_photo)
                    .crossFade()
                    .into(viewHolder.imageViewProduct);

            viewHolder.textProductName.setText(product.getNameThai());
            String strPrice = String.valueOf(product.getPrice()) + " บาท/" + product.getUnit();
            viewHolder.textProductPrice.setText(strPrice);
            viewHolder.textAmount.setText(String.valueOf(orderItem.getAmount()));
            viewHolder.textAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_select_amount);
                    dialog.setCancelable(true);

                    final EditText editAmount = (EditText) dialog.findViewById(R.id.edit_amount);
                    editAmount.setText(String.valueOf(orderItem.getAmount()));
                    Button btnOK = (Button) dialog.findViewById(R.id.btn_ok);

                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int newAmount = Integer.parseInt(editAmount.getText().toString());
                            editAmount(orderItem.getId(), newAmount);
                            orderItem.setAmount(newAmount);
                            orderItems.set(position, orderItem);
                            notifyDataSetChanged();
                            cartFragment.updateFooterView();
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });
        }

        viewHolder.textDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteThisOutOfCart(position);
            }
        });

        return convertView;
    }

    private void deleteThisOutOfCart(int position) {
        DatabaseDAO databaseDAO = new DatabaseDAO(mContext);
        databaseDAO.open();
        databaseDAO.deleteOrderItem(orderItems.get(position).getId());
        databaseDAO.close();
        orderItems.remove(position);
        products.remove(position);
        notifyDataSetChanged();
        cartFragment.updateFooterView();
    }

    private void editAmount(int orderId, int newAmount) {
        ContentValues values = new ContentValues();
        values.put(OrderItemTable.Columns._AMOUNT, newAmount);

        DatabaseDAO databaseDAO = new DatabaseDAO(mContext);
        databaseDAO.open();
        databaseDAO.updateOrderItemByValues(orderId, values);
        databaseDAO.close();
    }
}
