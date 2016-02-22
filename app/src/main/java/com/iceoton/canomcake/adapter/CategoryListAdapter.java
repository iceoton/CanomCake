package com.iceoton.canomcake.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iceoton.canomcake.R;
import com.iceoton.canomcake.model.Category;

import java.util.ArrayList;

public class CategoryListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Category> categories;

    public CategoryListAdapter(Context context, ArrayList<Category> categories) {
        this.mContext = context;
        this.categories = categories;
    }

    static class ViewHolder {
        public TextView text;
        public ImageView image;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return categories.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        // reuse views
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.category_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image_category);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();

        }

        return convertView;
    }


}
