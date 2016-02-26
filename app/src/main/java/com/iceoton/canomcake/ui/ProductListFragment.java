package com.iceoton.canomcake.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iceoton.canomcake.R;

public class ProductListFragment extends Fragment {
    Bundle bundle;

    public ProductListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_list, container, false);
        bundle = getArguments();
        initialView(rootView);

        return rootView;
    }

    private void initialView(View rootView) {
        int categoryId = bundle.getInt("category_id");
        TextView textView = (TextView) rootView.findViewById(R.id.text);
        textView.setText("Category id = " + categoryId);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

}
