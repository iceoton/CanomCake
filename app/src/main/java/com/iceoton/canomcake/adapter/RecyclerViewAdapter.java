package com.iceoton.canomcake.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iceoton.canomcake.R;
import com.iceoton.canomcake.activity.ProductDetailActivity;
import com.iceoton.canomcake.model.Product;
import com.iceoton.canomcake.util.AppPreference;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView txtName;
        TextView txtPrice;
        TextView txtSoldOut;

        public RecyclerViewHolder(final View itemView, final OnViewHolderClickListener listener) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.image_product);
            txtName = (TextView) itemView.findViewById(R.id.text_name);
            txtPrice = (TextView) itemView.findViewById(R.id.text_price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onHolderClick(itemView, getLayoutPosition());
                }
            });

            txtSoldOut = (TextView) itemView.findViewById(R.id.text_sold_out);
        }

        public interface OnViewHolderClickListener {
            void onHolderClick(View view, int position);
        }
    }

    private Context mContext;
    private ArrayList<Product> products;

    public RecyclerViewAdapter(Context context, ArrayList<Product> products) {
        this.mContext = context;
        this.products = products;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_product, null);
        RecyclerViewHolder rcv = new RecyclerViewHolder(cardView,
                new RecyclerViewHolder.OnViewHolderClickListener() {
                    @Override
                    public void onHolderClick(View view, int position) {
                        Intent intent = new Intent(mContext, ProductDetailActivity.class);
                        intent.putExtra("product_code", products.get(position).getCode());
                        mContext.startActivity(intent);
                    }
                });
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Product product = products.get(position);
        AppPreference preference = new AppPreference(mContext);
        String imageUrl = preference.getApiUrl()
                + product.getImageUrl();
        Glide.with(mContext)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivPhoto);
        holder.txtName.setText(product.getNameThai());
        holder.txtPrice.setText(product.getPrice() + " บาท/" + product.getUnit());
        if(product.getAvailable() < 1){
            holder.txtSoldOut.setVisibility(View.VISIBLE);
        } else{
            holder.txtSoldOut.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return this.products.size();
    }
}
