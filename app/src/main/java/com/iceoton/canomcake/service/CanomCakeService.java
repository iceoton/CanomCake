package com.iceoton.canomcake.service;

import com.iceoton.canomcake.model.response.GetAllCategoryResponse;
import com.iceoton.canomcake.model.response.GetAllProductResponse;
import com.iceoton.canomcake.model.response.GetCustomerResponse;
import com.iceoton.canomcake.model.response.GetOrderByCustomerResponse;
import com.iceoton.canomcake.model.response.GetOrderByIdResponse;
import com.iceoton.canomcake.model.response.GetProductByCategoryResponse;
import com.iceoton.canomcake.model.response.GetProductByCodeResponse;
import com.iceoton.canomcake.model.response.MakeOrderResponse;
import com.iceoton.canomcake.model.response.UserLoginResponse;

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

    @FormUrlEncoded
    @POST("customerApi.php")
    Call<GetOrderByCustomerResponse>  loadOrderByCustomerId(@Field("tag")String tag, @Field("data")String data);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<GetOrderByIdResponse> loadOrderByOrderId(@Field("tag")String tag, @Field("data")String data);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<GetCustomerResponse> loadCustomerByCustomerId(@Field("tag")String tag, @Field("data")String data);

}
