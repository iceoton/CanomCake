package com.iceoton.canomcake.fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.iceoton.canomcake.R;
import com.iceoton.canomcake.activity.CartActivity;
import com.iceoton.canomcake.activity.MainActivity;
import com.iceoton.canomcake.model.HistoryOrder;
import com.iceoton.canomcake.model.response.AddTransactionResponse;
import com.iceoton.canomcake.model.response.GetOrderByCustomerResponse;
import com.iceoton.canomcake.service.CanomCakeService;
import com.iceoton.canomcake.util.AppPreference;
import com.iceoton.canomcake.util.CartManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentConfirmFragment extends Fragment {
    TextView txtCountInCart;
    Spinner spinnerBankName, spinnerOrderId;
    EditText etMonty, etTransferDate, etTransferTime;

    Button btnSelectDate, btnSelectTime, btnConfirmTransfer;
    int mYear, mMonth, mDay, mHour, mMinute;

    ArrayList<String> waitingOrderIdTextList;
    ArrayAdapter<String> adapterOrderId;

    public PaymentConfirmFragment() {
        // Required empty public constructor
    }

    public static PaymentConfirmFragment newInstance(Bundle args) {
        PaymentConfirmFragment fragment = new PaymentConfirmFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_payment_confirm, container, false);
        rootView.setVisibility(View.INVISIBLE);
        initialView(rootView, savedInstanceState);

        return rootView;
    }

    private void initialActionBar() {
        ActionBar mActionBar = ((MainActivity) getActivity()).getSupportActionBar();
        ImageView imageTitle = (ImageView) mActionBar.getCustomView().findViewById(R.id.image_title);
        imageTitle.setImageResource(R.drawable.arrow_back);
        imageTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        TextView titleBar = (TextView) mActionBar.getCustomView().findViewById(R.id.text_title);
        titleBar.setText(getArguments().getString("title"));
        txtCountInCart = (TextView) mActionBar.getCustomView().findViewById(R.id.text_count);
        FrameLayout containerCart = (FrameLayout) mActionBar.getCustomView()
                .findViewById(R.id.container_cart);
        containerCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CartActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    private void initialView(View rootView, Bundle savedInstanceState) {
        initialActionBar();

        spinnerOrderId = (Spinner) rootView.findViewById(R.id.spinner_order_id);
        waitingOrderIdTextList = new ArrayList<String>();
        adapterOrderId = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, waitingOrderIdTextList);
        adapterOrderId.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrderId.setAdapter(adapterOrderId);
        loadWaitingOrder(rootView);

        spinnerBankName = (Spinner) rootView.findViewById(R.id.spinner_bank_name);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.bank_name_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBankName.setAdapter(adapter);

        etMonty = (EditText) rootView.findViewById(R.id.edit_monty);
        etTransferDate = (EditText) rootView.findViewById(R.id.edit_date);
        btnSelectDate = (Button) rootView.findViewById(R.id.button_select_date);
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        btnSelectTime = (Button) rootView.findViewById(R.id.button_select_time);
        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        etTransferTime = (EditText) rootView.findViewById(R.id.edit_time);

        btnConfirmTransfer = (Button) rootView.findViewById(R.id.button_confirm);
        btnConfirmTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrderConfirmBankTranfer();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        final CartManagement cartManagement = new CartManagement(getActivity());
        cartManagement.loadCountInto(txtCountInCart);
    }

    private void loadWaitingOrder(final View rootView) {
        AppPreference appPreference = new AppPreference(getActivity());
        JSONObject data = new JSONObject();
        try {
            data.put("customer_id", appPreference.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AppPreference preference = new AppPreference(getActivity());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(preference.getApiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.loadOrderByCustomerId("getOrderByCustomerID", data.toString());
        call.enqueue(new Callback<GetOrderByCustomerResponse>() {
            @Override
            public void onResponse(Call<GetOrderByCustomerResponse> call,
                                   Response<GetOrderByCustomerResponse> response) {
                ArrayList<HistoryOrder> historyOrders = response.body().getResult();
                if (historyOrders != null) {
                    Log.d("DEBUG", "The Number of history order = " + historyOrders.size());

                    for (HistoryOrder order : historyOrders) {
                        if (order.getStatus().equalsIgnoreCase("WAITING")) {
                            waitingOrderIdTextList.add("#" + String.valueOf(order.getId()));
                        }
                    }
                    Log.d("DEBUG", "The number of waiting order = " + waitingOrderIdTextList.size());
                    adapterOrderId.notifyDataSetChanged();
                    rootView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GetOrderByCustomerResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

    private void showDatePickerDialog() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        etTransferDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        etTransferTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void sendOrderConfirmBankTranfer() {
        if (etMonty.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(),"โปรดระบุจำนวนเงิน", Toast.LENGTH_SHORT).show();
        } else if (etTransferDate.getText().toString().equals("")) {
            Toast.makeText(getActivity(),"โปรดเลือกวันที่", Toast.LENGTH_SHORT).show();
        } else if (etTransferTime.getText().toString().equals("")) {
            Toast.makeText(getActivity(),"โปรดเลือกเวลา", Toast.LENGTH_SHORT).show();
        } else {

            String bankName = (String) spinnerBankName.getSelectedItem();
            String orderId = ((String) spinnerOrderId.getSelectedItem()).substring(1);
            double amount = Double.valueOf(etMonty.getText().toString());
            String dateTime = etTransferTime.getText().toString() + " " + etTransferDate.getText().toString();

            AppPreference appPreference = new AppPreference(getActivity());
            JSONObject data = new JSONObject();
            try {
                data.put("customer_id", appPreference.getUserId());
                data.put("order_id", orderId);
                data.put("bank_name", bankName);
                data.put("amount", amount);
                data.put("date_time", dateTime);
                data.put("bank_account", "");
                data.put("description", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("DEBUG", "JSON to confirm payment transfer = " + data.toString());
            AppPreference preference = new AppPreference(getActivity());
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(preference.getApiUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
            Call call = canomCakeService.sendPaymentConfirm("addTransaction", data.toString());
            call.enqueue(new Callback<AddTransactionResponse>() {
                @Override
                public void onResponse(Call<AddTransactionResponse> call, Response<AddTransactionResponse> response) {
                    if(response.body().getSuccessValue() == 1) {
                        showAlertDialog("แจ้งการชำระเงิน", "ทำการแจ้งการโอนเงินผ่านธนาคารเรียบร้อยแล้ว");
                    } else{
                        showAlertDialog("ทำการแจ้งไม่สำเร็จ", "โปรดตรวจสอบข้อมูล และลองใหม่อีกครั้ง");
                    }
                }

                @Override
                public void onFailure(Call<AddTransactionResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        }

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
                getActivity().onBackPressed();
            }
        });
        alertDialog.show();
    }

}
