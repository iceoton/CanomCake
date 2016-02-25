package com.iceoton.canomcake.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iceoton.canomcake.MainActivity;
import com.iceoton.canomcake.R;
import com.iceoton.canomcake.util.AppPreference;
import com.iceoton.canomcake.util.InternetConnection;

public class SplashFragment extends Fragment {
    Handler handler;
    Runnable runnable;
    long delay_time;
    long time = 2000L;
    InternetConnection connection;

    public SplashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_splash, container, false);
        initialView(rootView);
        connection = new InternetConnection(getActivity());

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                AppPreference appPreference = new AppPreference(getActivity());
                if (appPreference.getLoginStatus()) {
                    Intent intentToMain = new Intent(getActivity(), MainActivity.class);
                    startActivity(intentToMain);
                    getActivity().finish();
                } else {
                    Intent intentToLogin = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intentToLogin);
                    getActivity().finish();
                }

            }
        };

        return rootView;
    }

    private void initialView(View rootView) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (connection.isInternetAvailable(3000)) {
            delay_time = time;
            handler.postDelayed(runnable, delay_time);
            time = System.currentTimeMillis();
        } else {
            showAlertDialog("Internet not available", "เชื่อมต่ออินเทอร์เน็ตไม่ได้ ลองใหม่อีกครั้ง");
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        time = delay_time - (System.currentTimeMillis() - time);
    }

    /**
     * Function to display simple Alert Dialog
     *
     * @param title   - alert dialog title
     * @param message - alert message
     */
    public void showAlertDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "ลองใหม่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onResume();
            }
        });
        alertDialog.show();
    }
}
