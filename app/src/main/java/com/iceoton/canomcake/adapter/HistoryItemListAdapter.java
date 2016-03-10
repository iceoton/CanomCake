package com.iceoton.canomcake.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iceoton.canomcake.R;
import com.iceoton.canomcake.model.HistoryOrder;

import java.util.ArrayList;

public class HistoryItemListAdapter extends BaseAdapter{
    Context mContex;
    ArrayList<HistoryOrder> historyOrders;

    public HistoryItemListAdapter(Context mContex, ArrayList<HistoryOrder> historyOrders) {
        this.mContex = mContex;
        this.historyOrders = historyOrders;
    }

    static class ViewHolder {
        public TextView txtOrderId;
        public TextView txtDate;
        public TextView txtStatus;
        public ImageView imageStatus;
    }

    @Override
    public int getCount() {
        return historyOrders.size();
    }

    @Override
    public Object getItem(int position) {
        return historyOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return historyOrders.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_order, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtOrderId = (TextView) convertView.findViewById(R.id.text_order_id);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.text_date);
            viewHolder.txtStatus = (TextView) convertView.findViewById(R.id.text_status);
            viewHolder.imageStatus = (ImageView) convertView.findViewById(R.id.image_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        HistoryOrder historyOrder = historyOrders.get(position);
        viewHolder.txtOrderId.setText("#" + String.valueOf(historyOrder.getId()));
        viewHolder.txtDate.setText(historyOrder.getOrderTime());
        setStatus(viewHolder, historyOrder.getStatus());

        return convertView;
    }

    private void setStatus(ViewHolder viewHolder, String status){
        if(status.equalsIgnoreCase("DELIVERED")){
            viewHolder.txtStatus.setText(R.string.order_status_delivered);
            viewHolder.imageStatus.setImageResource(R.drawable.icon_delivered);
        } else if(status.equalsIgnoreCase("WAITING")){
            viewHolder.txtStatus.setText(R.string.order_status_waiting);
            viewHolder.imageStatus.setImageResource(R.drawable.icon_waiting);
        } else if(status.equalsIgnoreCase("DELIVERING")){
            viewHolder.txtStatus.setText(R.string.order_status_delivering);
            viewHolder.imageStatus.setImageResource(R.drawable.icon_delivering);
        } else if(status.equalsIgnoreCase("PAID")){
            viewHolder.txtStatus.setText(R.string.order_status_paid);
            viewHolder.imageStatus.setImageResource(R.drawable.icon_packaging);
        } else if(status.equalsIgnoreCase("CANCELED")){
            viewHolder.txtStatus.setText(R.string.order_status_canceled);
            viewHolder.imageStatus.setImageResource(R.drawable.icon_cancel);
        }
    }


}
