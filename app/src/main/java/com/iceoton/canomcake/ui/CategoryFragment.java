package com.iceoton.canomcake.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.iceoton.canomcake.R;
import com.iceoton.canomcake.adapter.CategoryListAdapter;
import com.iceoton.canomcake.model.GetAllCategoryResponse;
import com.iceoton.canomcake.service.CanomCakeService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryFragment extends Fragment {
    private ListView listViewCategory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        listViewCategory = (ListView) rootView.findViewById(R.id.list_category);

        loadCategories();

        return rootView;
    }

    private void loadCategories() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.loadCategories("getAllCategory");
        call.enqueue(new Callback<GetAllCategoryResponse>() {
            @Override
            public void onResponse(Call<GetAllCategoryResponse> call, Response<GetAllCategoryResponse> response) {
                Log.d("DEBUG", "Number of category = " + response.body().getResult().size());
                CategoryListAdapter categoryListAdapter
                        = new CategoryListAdapter(getContext(), response.body().getResult());
                listViewCategory.setAdapter(categoryListAdapter);
                listViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ((ProductFragment) getParentFragment()).showProductByCategory((int) id);
                    }
                });
            }

            @Override
            public void onFailure(Call<GetAllCategoryResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }
}


