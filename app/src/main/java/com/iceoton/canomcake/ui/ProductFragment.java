package com.iceoton.canomcake.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iceoton.canomcake.R;

public class ProductFragment extends Fragment {


    public ProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product, container, false);
        initialView(rootView);

        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .add(R.id.productContainer, new CategoryFragment())
                    .commit();
        }

        return rootView;
    }

    private void initialView(View rootView) {
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (getChildFragmentManager().getBackStackEntryCount() > 0) {
                        Log.d("DEBUG", "popping BackStack");
                        getChildFragmentManager().popBackStack();
                        return true;
                    } else {
                        Log.d("DEBUG", "nothing on BackStack");
                        return false;
                    }
                }

                return false;
            }
        });
    }

    public void showProductByCategory(int categoryId) {
        Log.d("DEBUG", "Show product in category id = " + categoryId);
        Bundle bundle = new Bundle();
        bundle.putInt("category_id", categoryId);
        Fragment fragment = new ProductListFragment();
        fragment.setArguments(bundle);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.productContainer, fragment)
                .addToBackStack(null)
                .commit();
    }



}
