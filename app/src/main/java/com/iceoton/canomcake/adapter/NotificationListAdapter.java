package com.iceoton.canomcake.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iceoton.canomcake.R;
import com.iceoton.canomcake.database.NotificationItem;

import java.util.ArrayList;

public class NotificationListAdapter extends BaseAdapter {
    Context mContex;
    ArrayList<NotificationItem> items;

    public NotificationListAdapter(Context mContex, ArrayList<NotificationItem> items) {
        this.mContex = mContex;
        this.items = items;
    }

    static class ViewHolder {
        public TextView txtMessage;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_notification, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtMessage = (TextView) convertView.findViewById(R.id.text_message);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NotificationItem item = items.get(position);
        viewHolder.txtMessage.setText(item.getMessage());

        return convertView;
    }

}
