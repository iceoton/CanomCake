package com.iceoton.canomcake.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
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
import com.iceoton.canomcake.adapter.HistoryItemListAdapter;
import com.iceoton.canomcake.model.HistoryOrder;
import com.iceoton.canomcake.model.response.GetOrderByCustomerResponse;
import com.iceoton.canomcake.service.CanomCakeService;
import com.iceoton.canomcake.util.AppPreference;
import com.iceoton.canomcake.util.CartManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoryFragment extends Fragment {
    TextView txtCountInCart;
    ListView listViewHistory;

    public static HistoryFragment newInstance(Bundle args) {
        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        initialView(rootView);

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

    private void initialView(View rootView) {
        initialActionBar();

        listViewHistory = (ListView) rootView.findViewById(R.id.list_history);
        loadHistory();
    }

    @Override
    public void onResume() {
        super.onResume();
        final CartManagement cartManagement = new CartManagement(getActivity());
        cartManagement.loadCountInto(txtCountInCart);
    }

    private void loadHistory() {
        AppPreference appPreference = new AppPreference(getActivity());
        JSONObject data = new JSONObject();
        try {
            data.put("customer_id", appPreference.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.loadOrderByCustomerId("getOrderByCustomerID", data.toString());
        call.enqueue(new Callback<GetOrderByCustomerResponse>() {
            @Override
            public void onResponse(Call<GetOrderByCustomerResponse> call,
                                   Response<GetOrderByCustomerResponse> response) {
                ArrayList<HistoryOrder> historyOrders = response.body().getResult();
                if (historyOrders != null) {
                    Log.d("DEBUG", "The Number of history = " + historyOrders.size());
                    HistoryItemListAdapter historyItemListAdapter = new HistoryItemListAdapter(getActivity(), historyOrders);
                    listViewHistory.setAdapter(historyItemListAdapter);
                    listViewHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            showOrderHistoryDetail((int)id);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetOrderByCustomerResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

    private void showOrderHistoryDetail(int orderId){
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtra("order_id", orderId);
        getActivity().startActivity(intent);
    }
}
