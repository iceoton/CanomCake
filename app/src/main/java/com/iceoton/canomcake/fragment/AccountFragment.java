package com.iceoton.canomcake.fragment;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.iceoton.canomcake.R;
import com.iceoton.canomcake.activity.LoginActivity;
import com.iceoton.canomcake.adapter.AccountMenuListAdapter;
import com.iceoton.canomcake.util.AppPreference;

public class AccountFragment extends Fragment {
    ListView listAccountMenu;

    public static Fragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        initialView(rootView);

        return rootView;
    }

    private void initialView(View rootView){
        listAccountMenu = (ListView)rootView.findViewById(R.id.list_account_menu);
        int[] imageIcon = getImageArray(R.array.account_menu_icon, R.mipmap.ic_launcher);
        String[] textName = getStringArray(R.array.account_menu_name);
        AccountMenuListAdapter accountMenuListAdapter
                = new AccountMenuListAdapter(getActivity(), textName, imageIcon);
        listAccountMenu.setAdapter(accountMenuListAdapter);
        listAccountMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eventItemClick(id);
            }
        });
    }

    private void eventItemClick(long id){
        Log.d("DEBUG", "Click account menu id=" + id);
        AppPreference appPreference  = new AppPreference(getActivity());
        if(id == R.drawable.logout_icon){
            appPreference.saveLoginStatus(false);
            Intent intentToLogin = new Intent(getActivity(), LoginActivity.class);
            startActivity(intentToLogin);
            getActivity().finish();
        } else {
            Toast.makeText(getActivity(), "ยังไม่พร้อมใช้งาน", Toast.LENGTH_SHORT).show();
        }
    }

    public int[] getImageArray(int resId, int defResId) {
        TypedArray my_image_array = getResources().obtainTypedArray(resId);
        int[] array_res = new int[my_image_array.length()];
        for(int i = 0 ; i < array_res.length ; i++)
            array_res[i] = my_image_array.getResourceId(i, defResId);
        my_image_array.recycle();
        return array_res;
    }

    public String[] getStringArray(int resId) {
        TypedArray my_string_array = getResources().obtainTypedArray(resId);
        String[] array_string = new String[my_string_array.length()];
        for(int i = 0 ; i < array_string.length ; i++)
            array_string[i] = my_string_array.getString(i);
        my_string_array.recycle();
        return array_string;
    }
}
