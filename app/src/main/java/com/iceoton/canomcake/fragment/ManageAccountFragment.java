package com.iceoton.canomcake.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.iceoton.canomcake.R;
import com.iceoton.canomcake.activity.CartActivity;
import com.iceoton.canomcake.activity.MainActivity;
import com.iceoton.canomcake.model.User;
import com.iceoton.canomcake.model.response.GetCustomerResponse;
import com.iceoton.canomcake.service.CanomCakeService;
import com.iceoton.canomcake.util.AppPreference;
import com.iceoton.canomcake.util.CartManagement;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManageAccountFragment extends Fragment {
    TextView txtCountInCart;
    EditText etName, etSurname, etMobile, etEmail, etAddress;
    Button btnEdit;

    public ManageAccountFragment() {
        // Required empty public constructor
    }

    public static ManageAccountFragment newInstance(Bundle args) {
        ManageAccountFragment fragment = new ManageAccountFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage_account, container, false);
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
        etName = (EditText) rootView.findViewById(R.id.edit_name);
        etSurname = (EditText) rootView.findViewById(R.id.edit_surname);
        etMobile = (EditText) rootView.findViewById(R.id.edit_mobile);
        etEmail = (EditText) rootView.findViewById(R.id.edit_email);
        etAddress = (EditText) rootView.findViewById(R.id.edit_address);
        btnEdit = (Button) rootView.findViewById(R.id.button_edit);

        loadProfileInfoFromServer();
    }

    @Override
    public void onResume() {
        super.onResume();
        final CartManagement cartManagement = new CartManagement(getActivity());
        cartManagement.loadCountInto(txtCountInCart);
    }

    private void loadProfileInfoFromServer() {
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
        Call call = canomCakeService.loadCustomerByCustomerId("getCustomerByCustomerId", data.toString());
        call.enqueue(new Callback<GetCustomerResponse>() {
            @Override
            public void onResponse(Call<GetCustomerResponse> call, Response<GetCustomerResponse> response) {
                User user = response.body().getResult();
                if (user != null) {
                    showUserInfo(user);
                }
            }

            @Override
            public void onFailure(Call<GetCustomerResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

    private void showUserInfo(final User user){
        int cutIndex = user.getFullname().indexOf(",");
        String name = user.getFullname().substring(0, cutIndex);
        String surname = user.getFullname().substring(cutIndex + 1, user.getFullname().length());
        etName.setText(name);
        etSurname.setText(surname);
        etMobile.setText(user.getPhoneNumber());
        etEmail.setText(user.getEmail());
        etEmail.setEnabled(false);
        etAddress.setText(user.getAddress());
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo(user);
            }
        });
    }


    private void updateUserInfo(User user){
        
    }

}
