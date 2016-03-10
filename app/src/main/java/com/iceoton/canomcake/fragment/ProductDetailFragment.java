package com.iceoton.canomcake.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iceoton.canomcake.R;
import com.iceoton.canomcake.activity.CartActivity;
import com.iceoton.canomcake.activity.ProductDetailActivity;
import com.iceoton.canomcake.database.DatabaseDAO;
import com.iceoton.canomcake.database.OrderItem;
import com.iceoton.canomcake.model.GetProductByCodeResponse;
import com.iceoton.canomcake.model.Product;
import com.iceoton.canomcake.service.CanomCakeService;
import com.iceoton.canomcake.util.CartManagement;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDetailFragment extends Fragment {
    ImageView ivPhoto;
    TextView txtName, txtDetail, txtPrice, txtUnit;
    Button btnAddToCart;
    TextView titleBar;
    TextView txtCountInCart;

    public static ProductDetailFragment newInstance(Bundle args) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_detail, container, false);
        initialView(rootView, savedInstanceState);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        final CartManagement cartManagement = new CartManagement(getActivity());
        cartManagement.loadCountInto(txtCountInCart);
    }

    private void initialView(View rootView, Bundle savedInstanceState) {
        Bundle args = getArguments();
        String productCode = args.getString("product_code");
        ivPhoto = (ImageView) rootView.findViewById(R.id.image_photo);
        txtName = (TextView) rootView.findViewById(R.id.text_name);
        txtDetail = (TextView) rootView.findViewById(R.id.text_detail);
        txtPrice = (TextView) rootView.findViewById(R.id.text_price);
        txtUnit = (TextView) rootView.findViewById(R.id.text_unit);
        btnAddToCart = (Button) rootView.findViewById(R.id.btn_add_to_cart);

        ActionBar mActionBar = ((ProductDetailActivity) getActivity()).getSupportActionBar();
        View customView = getLayoutInflater(savedInstanceState).inflate(R.layout.custom_actionbar, null);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0,0);

        ImageView imageTitle = (ImageView) customView.findViewById(R.id.image_title);
        imageTitle.setImageResource(R.drawable.arrow_back);
        imageTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        titleBar = (TextView) customView.findViewById(R.id.text_title);
        txtCountInCart = (TextView) mActionBar.getCustomView().findViewById(R.id.text_count);
        FrameLayout containerCart = (FrameLayout) customView.findViewById(R.id.container_cart);
        containerCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CartActivity.class);
                getActivity().startActivity(intent);
            }
        });

        loadProductByCode(productCode);
    }

    private void loadProductByCode(final String productCode) {
        JSONObject data = new JSONObject();
        try {
            data.put("code", productCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.loadProductByCode("getProductByCode", data.toString());
        call.enqueue(new Callback<GetProductByCodeResponse>() {
            @Override
            public void onResponse(Call<GetProductByCodeResponse> call, Response<GetProductByCodeResponse> response) {
                final Product product = response.body().getResult();
                if (product != null) {
                    txtName.setText(product.getNameThai());
                    titleBar.setText(product.getNameThai());
                    txtDetail.setText(product.getDetail());
                    txtPrice.setText(Double.toString(product.getPrice()));
                    txtUnit.setText("บาท/" + product.getUnit());
                    btnAddToCart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addProductToCart(product);
                        }
                    });

                    String imageUrl = getActivity().getResources().getString(R.string.api_url)
                            + product.getImageUrl();
                    Glide.with(getActivity()).load(imageUrl).into(ivPhoto);
                }
            }

            @Override
            public void onFailure(Call<GetProductByCodeResponse> call, Throwable t) {

            }
        });
    }

    private void addProductToCart(Product product){
        OrderItem orderItem = new OrderItem();
        orderItem.setProductCode(product.getCode());
        orderItem.setAmount(1);
        DatabaseDAO databaseDAO = new DatabaseDAO(getActivity());
        databaseDAO.open();
        databaseDAO.addOrderItem(orderItem);
        // update cart
        CartManagement cartManagement = new CartManagement(getActivity());
        cartManagement.loadCountInto(txtCountInCart);
        //show dialog to checkout
        databaseDAO.close();
        // exit this fragment
        if (!getActivity().getSupportFragmentManager().popBackStackImmediate()) {
            getActivity().supportFinishAfterTransition();
        }
    }




}
