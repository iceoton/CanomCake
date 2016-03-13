package com.iceoton.canomcake.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.iceoton.canomcake.R;
import com.iceoton.canomcake.activity.CartActivity;
import com.iceoton.canomcake.activity.MainActivity;
import com.iceoton.canomcake.util.CartManagement;

public class NotificationFragment extends Fragment {
    TextView txtCountInCart;

    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance(Bundle args) {
        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        initialView(rootView, savedInstanceState);

        return rootView;
    }

    private void initialActionBar() {
        ActionBar mActionBar = ((MainActivity) getActivity()).getSupportActionBar();
        ImageView imageTitle = (ImageView) mActionBar.getCustomView().findViewById(R.id.image_title);
        imageTitle.setImageResource(R.drawable.arrow_back);
        imageTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        TextView titleBar = (TextView) mActionBar.getCustomView().findViewById(R.id.text_title);
        titleBar.setText(getArguments().getString("title"));
        txtCountInCart = (TextView) mActionBar.getCustomView().findViewById(R.id.text_count);
        FrameLayout containerCart = (FrameLayout) mActionBar.getCustomView()
                .findViewById(R.id.container_cart);
        containerCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CartActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    private void initialView(View rootView, Bundle savedInstanceState) {
        initialActionBar();

    }

    @Override
    public void onResume() {
        super.onResume();
        final CartManagement cartManagement = new CartManagement(getActivity());
        cartManagement.loadCountInto(txtCountInCart);
    }

}
