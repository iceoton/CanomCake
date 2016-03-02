package com.iceoton.canomcake.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iceoton.canomcake.R;
import com.iceoton.canomcake.activity.CartActivity;
import com.iceoton.canomcake.adapter.OrderItemListAdapter;
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

public class CartFragment extends Fragment {
    ListView listOrderItem;
    View footerView;
    ArrayList<OrderItem> orderItems;
    ArrayList<Product> products;
    int loadItemCount;

    public static CartFragment newInstance(Bundle args) {
        CartFragment fragment = new CartFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        initialActionBar();
        initialView(rootView);

        return rootView;
    }

    private void initialView(View rootView) {
        DatabaseDAO databaseDAO = new DatabaseDAO(getActivity());
        databaseDAO.open();
        orderItems = databaseDAO.getAllOrderItem();
        databaseDAO.close();

        listOrderItem = (ListView) rootView.findViewById(R.id.list_item);
        footerView = getLayoutInflater(null).inflate(R.layout.footer_list_of_item, null, false);
        listOrderItem.addFooterView(footerView);

        loadItemCount = 0;
        products = new ArrayList<>(orderItems.size());
        Log.d("DEBUG", "Product lis size is " + products.size());
        for(int i = 0; i < orderItems.size(); i++){
            products.add(null); //initial list item in this position
            loadProductFromServer(i);
        }

        Button btnMakeOrder = (Button) footerView.findViewById(R.id.btn_make_order);
        btnMakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "สั่งซื้อสินค้าเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
            }
        });
        updateFooterView();
    }

    private void initialActionBar(){
        ActionBar mActionBar = ((CartActivity)getActivity()).getSupportActionBar();
        mActionBar.setCustomView(R.layout.custom_actionbar);
        mActionBar.setDisplayShowCustomEnabled(true);
        ImageView imageTitle = (ImageView) mActionBar.getCustomView().findViewById(R.id.image_title);
        imageTitle.setImageResource(R.drawable.arrow_back);
        imageTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        TextView titleBar = (TextView)mActionBar.getCustomView().findViewById(R.id.text_title);
        FrameLayout containerCart = (FrameLayout) mActionBar.getCustomView()
                .findViewById(R.id.container_cart);
        containerCart.setVisibility(View.GONE);
        titleBar.setText("สินค้าในรถเข็น");
    }

    private void updateFooterView(){
        int totalAmount = 0;
        for (int i = 0; i < orderItems.size(); i++){
            totalAmount += orderItems.get(i).getAmount();
        }
        TextView txtTotalAmount = (TextView) footerView.findViewById(R.id.text_total_amount);
        txtTotalAmount.setText(String.valueOf(totalAmount));

    }

    private void loadProductFromServer(final int position) {
        Log.d("DEBUG", "load image from server");
        JSONObject data = new JSONObject();
        try {
            data.put("code", orderItems.get(position).getProductCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getContext().getResources().getString(R.string.api_url))
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
                    products.set(position, product);
                    loadItemCount++;
                    if(loadItemCount == orderItems.size()){
                        OrderItemListAdapter itemListAdapter = new OrderItemListAdapter(getActivity(), orderItems, products);
                        listOrderItem.setAdapter(itemListAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProductByCodeResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }


}
