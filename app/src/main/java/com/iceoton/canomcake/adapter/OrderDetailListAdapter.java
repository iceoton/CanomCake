package com.iceoton.canomcake.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iceoton.canomcake.R;
import com.iceoton.canomcake.model.OrderDetailItem;

import java.util.ArrayList;

public class OrderDetailListAdapter extends BaseAdapter{
    Context mContext;
    ArrayList<OrderDetailItem> orderDetailItems;

    public OrderDetailListAdapter(Context mContext, ArrayList<OrderDetailItem> orderDetailItems) {
        this.mContext = mContext;
        this.orderDetailItems = orderDetailItems;
    }

    static class ViewHolder {
        public ImageView imageViewProduct;
        public TextView textProductName, textAmount;

        public ViewHolder(View convertView) {
            this.imageViewProduct = (ImageView) convertView.findViewById(R.id.image_product);
            this.textProductName = (TextView) convertView.findViewById(R.id.text_name);
            this.textAmount = (TextView) convertView.findViewById(R.id.text_amount);
        }
    }

    @Override
    public int getCount() {
        return orderDetailItems.size();
    }

    @Override
    public OrderDetailItem getItem(int position) {
        return orderDetailItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_order_detail, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        OrderDetailItem orderDetailItem = orderDetailItems.get(position);
        viewHolder.textProductName.setText(orderDetailItem.getNameThai());
        viewHolder.textAmount.setText(String.valueOf(orderDetailItem.getAmount()));

        String imageUrl = mContext.getResources().getString(R.string.api_url)
                + orderDetailItem.getImageUrl();
        Glide.with(mContext).load(imageUrl)
                .placeholder(R.drawable.product_photo)
                .crossFade()
                .into(viewHolder.imageViewProduct);

        return convertView;
    }
}
