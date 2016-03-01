package com.iceoton.canomcake.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iceoton.canomcake.R;
import com.iceoton.canomcake.database.DatabaseDAO;
import com.iceoton.canomcake.database.OrderItem;
import com.iceoton.canomcake.model.GetProductByCodeResponse;
import com.iceoton.canomcake.model.Product;
import com.iceoton.canomcake.service.CanomCakeService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderItemListAdapter extends BaseAdapter {

    static class ViewHolder {
        public ImageView imageViewProduct;
        public TextView textProductName, textProductPrice, textDelete;
        public EditText editTextAmount;

        public ViewHolder(View convertView) {
            this.imageViewProduct = (ImageView) convertView.findViewById(R.id.image_product);
            this.textProductName = (TextView) convertView.findViewById(R.id.text_name);
            this.textProductPrice = (TextView) convertView.findViewById(R.id.text_price);
            this.editTextAmount = (EditText) convertView.findViewById(R.id.edit_amount);
            this.textDelete = (TextView) convertView.findViewById(R.id.text_delete);
        }
    }

    Context mContext;
    ArrayList<OrderItem> orderItems;

    public OrderItemListAdapter(Context mContext, ArrayList<OrderItem> orderItems) {
        this.mContext = mContext;
        this.orderItems = orderItems;
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
            convertView = inflater.inflate(R.layout.order_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Log.d("DEBUG", "get item " + position);
        OrderItem orderItem = orderItems.get(position);
        // load product by product_code
        loadProductFromServer(orderItem, viewHolder);

        viewHolder.textDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteThisOutOfCart(position);
            }
        });

        return convertView;
    }

    private void loadProductFromServer(final OrderItem orderItem, final ViewHolder viewHolder) {
        Log.d("DEBUG", "load image from server");
        JSONObject data = new JSONObject();
        try {
            data.put("code", orderItem.getProductCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mContext.getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.loadProductByCode("getProductByCode", data.toString());
        call.enqueue(new Callback<GetProductByCodeResponse>() {
            @Override
            public void onResponse(Call<GetProductByCodeResponse> call, Response<GetProductByCodeResponse> response) {
                final Product product = response.body().getResult();
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
                    viewHolder.editTextAmount.setText(String.valueOf(orderItem.getAmount()));
                }
            }

            @Override
            public void onFailure(Call<GetProductByCodeResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

    private void deleteThisOutOfCart(int position) {
        notifyDataSetChanged();
        DatabaseDAO databaseDAO = new DatabaseDAO(mContext);
        databaseDAO.open();
        databaseDAO.deleteOrderItem(orderItems.get(position).getId());
        databaseDAO.close();
        orderItems.remove(position);
    }
}
