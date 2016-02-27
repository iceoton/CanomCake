package com.iceoton.canomcake.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iceoton.canomcake.R;
import com.iceoton.canomcake.model.ItemObject;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolders> {

    public static class RecyclerViewHolders extends RecyclerView.ViewHolder {
        ImageView ivProductPhoto;
        TextView txtProductName;

        public RecyclerViewHolders(View itemView) {
            super(itemView);
            ivProductPhoto = (ImageView) itemView.findViewById(R.id.image_product);
            txtProductName = (TextView) itemView.findViewById(R.id.text_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(),
                            "Clicked product position = " + getLayoutPosition(),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
    }

    private Context mContext;
    private List<ItemObject> itemList;

    public RecyclerViewAdapter(Context context, List<ItemObject> itemList) {
        this.mContext = context;
        this.itemList = itemList;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_product, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(cardView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.ivProductPhoto.setImageResource(itemList.get(position).getPhoto());
        holder.txtProductName.setText(itemList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
