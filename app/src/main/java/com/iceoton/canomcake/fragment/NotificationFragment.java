package com.iceoton.canomcake.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.iceoton.canomcake.R;
import com.iceoton.canomcake.activity.CartActivity;
import com.iceoton.canomcake.activity.MainActivity;
import com.iceoton.canomcake.activity.OrderDetailActivity;
import com.iceoton.canomcake.adapter.NotificationListAdapter;
import com.iceoton.canomcake.database.DatabaseDAO;
import com.iceoton.canomcake.database.NotificationItem;
import com.iceoton.canomcake.util.CartManagement;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {
    TextView txtCountInCart;
    ListView lvNotification;

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

        lvNotification = (ListView) rootView.findViewById(R.id.list_notification);
        DatabaseDAO dao = new DatabaseDAO(getActivity());
        dao.open();
        final ArrayList<NotificationItem> notificationItems = dao.getAllNotificationItem();
        dao.close();
        NotificationListAdapter listAdapter = new NotificationListAdapter(getActivity(), notificationItems);
        lvNotification.setAdapter(listAdapter);
        lvNotification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("order_id", Integer.parseInt(notificationItems.get(position).getOrderId()));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        final CartManagement cartManagement = new CartManagement(getActivity());
        cartManagement.loadCountInto(txtCountInCart);
    }

}
