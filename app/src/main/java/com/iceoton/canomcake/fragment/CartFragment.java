package com.iceoton.canomcake.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
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

import java.util.ArrayList;

public class CartFragment extends Fragment {
    ListView listOrderItem;
    View footerView;
    ArrayList<OrderItem> orderItems;

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

        OrderItemListAdapter itemListAdapter = new OrderItemListAdapter(getActivity(), orderItems);
        listOrderItem.setAdapter(itemListAdapter);

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



}
