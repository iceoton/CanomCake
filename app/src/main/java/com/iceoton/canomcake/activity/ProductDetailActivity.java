package com.iceoton.canomcake.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.iceoton.canomcake.R;
import com.iceoton.canomcake.fragment.ProductDetailFragment;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        String productCode = getIntent().getStringExtra("product_code");

        Bundle args = new Bundle();
        args.putString("product_code", productCode);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, ProductDetailFragment.newInstance(args))
                    .commit();
        }
    }


}
