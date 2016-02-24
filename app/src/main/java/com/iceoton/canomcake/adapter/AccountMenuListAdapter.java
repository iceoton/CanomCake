package com.iceoton.canomcake.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iceoton.canomcake.R;

public class AccountMenuListAdapter extends BaseAdapter {
    private Context mContext;
    private String[] arrayName;
    private int[] iconId;

    static class ViewHolder {
        public TextView textName;
        public ImageView imageIcon;
    }

    public AccountMenuListAdapter(Context mContext, String[] arrayName, int[] iconId) {
        this.mContext = mContext;
        this.arrayName = arrayName;
        this.iconId = iconId;
    }

    @Override
    public int getCount() {
        return arrayName.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return iconId[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.account_menu_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageIcon = (ImageView) convertView.findViewById(R.id.image_icon);
            viewHolder.textName = (TextView) convertView.findViewById(R.id.text_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageIcon.setImageResource(iconId[position]);
        viewHolder.textName.setText(arrayName[position]);

        return convertView;
    }
}
