package com.iceoton.canomcake.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iceoton.canomcake.MainActivity;
import com.iceoton.canomcake.R;

public class SplashFragment extends Fragment {
    Handler handler;
    Runnable runnable;
    long delay_time;
    long time = 3000L;

    public SplashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_splash, container, false);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intentToMain = new Intent(getActivity(), MainActivity.class);
                startActivity(intentToMain);
                getActivity().finish();
            }
        };

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        delay_time = time;
        handler.postDelayed(runnable, delay_time);
        time = System.currentTimeMillis();
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        time = delay_time - (System.currentTimeMillis() - time);
    }
}
