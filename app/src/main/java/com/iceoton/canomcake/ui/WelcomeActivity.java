package com.iceoton.canomcake.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.iceoton.canomcake.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, new SplashFragment())
                    .commit();
        }
    }
}
