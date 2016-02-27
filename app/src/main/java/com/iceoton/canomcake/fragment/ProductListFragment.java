package com.iceoton.canomcake.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iceoton.canomcake.R;
import com.iceoton.canomcake.activity.MainActivity;
import com.iceoton.canomcake.adapter.RecyclerViewAdapter;
import com.iceoton.canomcake.model.ItemObject;

import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment {
    Bundle bundle;
    RecyclerView recyclerView;

    public static ProductListFragment newInstance(Bundle args){
        ProductListFragment fragment = new ProductListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_list, container, false);
        initialView(rootView);

        return rootView;
    }

    private void initialView(View rootView) {
        bundle = getArguments();
        int categoryId = bundle.getInt("category_id");
        String categoryName = bundle.getString("category_name");

        ActionBar mActionBar = ((MainActivity)getActivity()).getSupportActionBar();
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
        titleBar.setText(categoryName);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        loadProductInCategory(categoryId);
    }

    private void loadProductInCategory(int categoryId){
        List<ItemObject> rowListItem = getAllItemList();
        GridLayoutManager lLayout = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(lLayout);
        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(getContext(), rowListItem);
        recyclerView.setAdapter(rcAdapter);
    }

    private List<ItemObject> getAllItemList(){

        List<ItemObject> allItems = new ArrayList<ItemObject>();
        for(int i = 0; i < 20; i++) {
            allItems.add(new ItemObject("ชื่อสินค้า", R.drawable.product_image));
        }

        return allItems;
    }

}
