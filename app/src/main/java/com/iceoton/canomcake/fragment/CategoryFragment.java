package com.iceoton.canomcake.fragment;

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
import com.iceoton.canomcake.activity.MainActivity;
import com.iceoton.canomcake.adapter.CategoryListAdapter;
import com.iceoton.canomcake.model.Category;
import com.iceoton.canomcake.model.response.GetAllCategoryResponse;
import com.iceoton.canomcake.service.CanomCakeService;
import com.iceoton.canomcake.util.AppPreference;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryFragment extends Fragment {
    private ListView listViewCategory;

    public static Fragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        listViewCategory = (ListView) rootView.findViewById(R.id.list_category);

        loadCategories();
        Log.d("DEBUG", "onCreateView in CategoryFragment");
        return rootView;
    }

    private void loadCategories() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.v("DEBUG", message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        AppPreference preference = new AppPreference(getActivity());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(preference.getApiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        CanomCakeService canomCakeService = retrofit.create(CanomCakeService.class);
        Call call = canomCakeService.loadCategories("getAllCategory");
        call.enqueue(new Callback<GetAllCategoryResponse>() {
            @Override
            public void onResponse(Call<GetAllCategoryResponse> call, Response<GetAllCategoryResponse> response) {
                Log.d("DEBUG", "Number of category = " + response.body().getResult().size());
                final ArrayList<Category> categories = response.body().getResult();
                if (categories != null) {
                    CategoryListAdapter categoryListAdapter = new CategoryListAdapter(getContext(), categories);
                    listViewCategory.setAdapter(categoryListAdapter);
                    listViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            showProductByCategory(categories.get(position));
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetAllCategoryResponse> call, Throwable t) {
                Log.d("DEBUG", "Call CanomCake-API failure." + "\n" + t.getMessage());
            }
        });
    }

    public void showProductByCategory(Category category) {
        Log.d("DEBUG", "Show product in category id = " + category.getId());
        Bundle bundle = new Bundle();
        bundle.putInt("category_id", category.getId());
        bundle.putString("category_name", category.getNameThai());
        ((MainActivity) getActivity()).placeFragmentToContainer(ProductListFragment.newInstance(bundle));
    }

}

