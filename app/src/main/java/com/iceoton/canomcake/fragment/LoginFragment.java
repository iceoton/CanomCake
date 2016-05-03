package com.iceoton.canomcake.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iceoton.canomcake.R;
import com.iceoton.canomcake.activity.MainActivity;
import com.iceoton.canomcake.activity.SetIpActivity;
import com.iceoton.canomcake.model.User;
import com.iceoton.canomcake.model.response.UserLoginResponse;
import com.iceoton.canomcake.service.CanomCakeService;
import com.iceoton.canomcake.util.AppPreference;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends Fragment {
    EditText etUsername, etPassword;
    Button btnLogin, btnRegister;
    TextView txtForgetPassword;
    ImageView imgLogo;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initialView(rootView);

        return rootView;
    }

    private void initialView(View rootView) {
        etUsername = (EditText) rootView.findViewById(R.id.username);
        etPassword = (EditText) rootView.findViewById(R.id.password);
        btnLogin = (Button) rootView.findViewById(R.id.button_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginToServer(etUsername.getText().toString().trim()
                        , etPassword.getText().toString());
            }
        });
        btnRegister = (Button) rootView.findViewById(R.id.button_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentContainer, RegisterFragment.newInstance(null))
                        .addToBackStack(null)
                        .commit();
            }
        });

        txtForgetPassword = (TextView) rootView.findViewById(R.id.text_forget_password);
        txtForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentContainer, ForgetPasswordFragment.newInstance(null))
                        .addToBackStack(null)
                        .commit();
            }
        });

        imgLogo = (ImageView) rootView.findViewById(R.id.image_logo);
        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetIpActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loginToServer(String username, String password) {
        JSONObject data = new JSONObject();
        try {
            data.put("username", username);
            data.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AppPreference preference = new AppPreference(getActivity());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(preference.getApiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.loginToServer("loginCustomer", data.toString());
        call.enqueue(new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {

                if (response.body().getUser() != null) {
                    User user = response.body().getUser();
                    Log.d("DEBUG", "id = " + user.getId());
                    AppPreference appPreference = new AppPreference(getActivity());
                    appPreference.saveUserId(user.getId());
                    appPreference.saveUserName(user.getEmail());
                    appPreference.saveLoginStatus(true);

                    startMainActivity();
                } else {
                    Log.d("DEBUG", "Login error: " + response.body().getErrorMessage());
                    Toast.makeText(getActivity(), "ไม่พบอีเมลนี้หรือรหัสผ่านไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }


    private void startMainActivity() {
        Intent intentToMain = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(intentToMain);
        getActivity().finish();
    }

}
