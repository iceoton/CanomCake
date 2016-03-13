package com.iceoton.canomcake.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.iceoton.canomcake.R;
import com.iceoton.canomcake.fragment.OrderDetailFragment;

public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        int orderId = getIntent().getIntExtra("order_id", 0);

        Bundle args = new Bundle();
        args.putInt("order_id", orderId);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, OrderDetailFragment.newInstance(args))
                    .commit();
        }
    }
}
