package com.iceoton.canomcake.service;

import com.iceoton.canomcake.model.GetAllCategoryResponse;
import com.iceoton.canomcake.model.GetAllProductResponse;
import com.iceoton.canomcake.model.GetProductByCategoryResponse;
import com.iceoton.canomcake.model.GetProductByCodeResponse;
import com.iceoton.canomcake.model.MakeOrderResponse;
import com.iceoton.canomcake.model.UserLoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CanomCakeService {

    @FormUrlEncoded
    @POST("customerApi.php")
    Call<UserLoginResponse> loginToServer(@Field("tag") String tag,
                                          @Field("data") String data);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<GetAllCategoryResponse> loadCategories(@Field("tag") String tag);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<GetProductByCategoryResponse> loadProductByCategoryId(@Field("tag") String tag,
                                                               @Field("data") String data);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<GetAllProductResponse> loadAllProduct(@Field("tag") String tag);

    @FormUrlEncoded
    @POST("customerApi.php")
    Call<GetProductByCodeResponse> loadProductByCode(@Field("tag") String tag,
                                                     @Field("data") String data);

    @FormUrlEncoded
    @POST("customerApi.php")
    Call<MakeOrderResponse> makeOrderToServer(@Field("tag") String tag, @Field("data") String data);
}
