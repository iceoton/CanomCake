package com.iceoton.canomcake.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.iceoton.canomcake.R;
import com.iceoton.canomcake.activity.MainActivity;
import com.iceoton.canomcake.util.AppPreference;

public class SetIpFragment extends Fragment {
    EditText etUrl;
    Button btnSave;

    public static SetIpFragment newInstance() {
        SetIpFragment fragment = new SetIpFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_set_ip, container, false);
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
        titleBar.setVisibility(View.INVISIBLE);
        FrameLayout containerCart = (FrameLayout) mActionBar.getCustomView()
                .findViewById(R.id.container_cart);
        containerCart.setVisibility(View.INVISIBLE);
    }

    private void initialView(View rootView) {
        //initialActionBar();
        final AppPreference preference = new AppPreference(getActivity());
        etUrl = (EditText)rootView.findViewById(R.id.edit_url);
        etUrl.setText(preference.getApiUrl());
        btnSave = (Button) rootView.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = etUrl.getText().toString().trim();
                preference.saveApiUrl(url);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
