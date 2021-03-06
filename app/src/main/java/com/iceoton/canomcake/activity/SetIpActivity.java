package com.iceoton.canomcake.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.iceoton.canomcake.R;
import com.iceoton.canomcake.fragment.SetIpFragment;

public class SetIpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, SetIpFragment.newInstance())
                    .commit();
        }
    }
}
