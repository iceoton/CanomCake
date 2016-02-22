package com.iceoton.canomcake.service;

import com.iceoton.canomcake.model.GetAllCategoryResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by jaturon on 2/21/2016 AD.
 */
public interface CanomCakeService {

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<GetAllCategoryResponse> loadCategories(@Field("tag") String tag);
}
