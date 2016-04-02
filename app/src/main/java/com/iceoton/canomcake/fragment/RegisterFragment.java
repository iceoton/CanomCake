package com.iceoton.canomcake.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iceoton.canomcake.R;
import com.iceoton.canomcake.model.User;
import com.iceoton.canomcake.model.response.RegisterCustomerResponse;
import com.iceoton.canomcake.service.CanomCakeService;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterFragment extends Fragment {
    TextView txtCountInCart;
    EditText etName, etSurname, etMobile, etEmail, etAddress, etPassword, etConfirmPassword;
    Button btnRegister;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance(Bundle args) {
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        initialView(rootView, savedInstanceState);

        return rootView;
    }

    private void initialView(View rootView, Bundle savedInstanceState) {
        etName = (EditText) rootView.findViewById(R.id.edit_name);
        etSurname = (EditText) rootView.findViewById(R.id.edit_surname);
        etMobile = (EditText) rootView.findViewById(R.id.edit_mobile);
        etEmail = (EditText) rootView.findViewById(R.id.edit_email);
        etAddress = (EditText) rootView.findViewById(R.id.edit_address);
        btnRegister = (Button) rootView.findViewById(R.id.button_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputData()){
                    registerUserFromServer();
                }
            }
        });
        etPassword = (EditText) rootView.findViewById(R.id.edit_password);
        etConfirmPassword = (EditText) rootView.findViewById(R.id.edit_confirm_password);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private boolean checkInputData(){
        boolean flag = true;
        if(etEmail.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), "กรุณากรอกอีเมล", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if(etPassword.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), "กรุณากรอกรหัสผ่าน", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if(etName.getText().toString().trim().equals("") || etSurname.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), "กรุณากรอกชื่อและนามสกุล", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if(!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())){
            Toast.makeText(getActivity(), "รหัสผ่านกับยืนยันรหัสผ่านไม่ตรงกัน", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if(etAddress.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), "กรุณากรอกที่อยู่", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if(etMobile.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), "กรุณากรอกเบอร์โทร", Toast.LENGTH_SHORT).show();
            flag = false;
        }

        return flag;
    }

    private void registerUserFromServer() {
        JSONObject data = new JSONObject();
        try {
            data.put("email", etEmail.getText().toString().trim());
            data.put("password",etPassword.getText().toString().trim());
            String fullName = etName.getText().toString().trim() + "," + etSurname.getText().toString().trim();
            data.put("fullname", fullName);
            data.put("phone_number", etMobile.getText().toString().trim());
            data.put("address", etAddress.getText().toString().trim());
            data.put("latitude", "");
            data.put("longitude", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.registerCustomer("registerCustomer", data.toString());
        call.enqueue(new Callback<RegisterCustomerResponse>() {
            @Override
            public void onResponse(Call<RegisterCustomerResponse> call, Response<RegisterCustomerResponse> response) {
                User user = response.body().getResult();
                if (user != null) {
                    showAlertDialog("Register", "สมัครสมาชิกเรียบร้อยแล้ว " +
                            "\nกรุณาคลิกยืนยันตัวตนผ่านทาง email ที่ส่งไป");
                    getActivity().onBackPressed();
                    /*
                    AppPreference appPreference = new AppPreference(getActivity());
                    appPreference.saveUserId(user.getId());
                    appPreference.saveUserName(user.getEmail());
                    appPreference.saveLoginStatus(true);
                    Intent intentToMain = new Intent(getActivity(), MainActivity.class);
                    getActivity().startActivity(intentToMain);
                    getActivity().finish();
                    */
                } else{
                    showAlertDialog("Register", response.body().getErrorMessage());
                }
            }

            @Override
            public void onFailure(Call<RegisterCustomerResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

    /**
     * Function to display simple Alert Dialog
     *
     * @param title   - alert dialog title
     * @param message - alert message
     */
    public void showAlertDialog(String title, String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }

}
