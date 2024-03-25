package com.example.nikestore.ApiServices;

import com.example.nikestore.Model.AddCommentResponse;
import com.example.nikestore.Model.AddToCartResponse;
import com.example.nikestore.Model.Banner;
import com.example.nikestore.Model.BilingResponse;
import com.example.nikestore.Model.CartItemCount;
import com.example.nikestore.Model.CartResponse;
import com.example.nikestore.Model.CheckoutResponse;
import com.example.nikestore.Model.Comment;
import com.example.nikestore.Model.MessageResponse;
import com.example.nikestore.Model.OrderItem;
import com.example.nikestore.Model.OrderItemList;
import com.example.nikestore.Model.Product;
import com.example.nikestore.Model.TokenResponse;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitApiClass {
    @GET("product/list")
    Single<List<Product>> getProducts(@Query("sort") String sort);

    @GET("banner/slider")
    Single<List<Banner>> getBanners();

    @GET("comment/list")
    Single<List<Comment>> getComments(@Query("product_id") int productId);

    @POST("comment/add")
    Single<AddCommentResponse> leaveComment(@Header("Authorization") String token , @Body JsonObject jsonObject);

    @POST("order/list")
    Single<OrderItemList> getOrderList(@Header("Authorization") String token);

    @POST("auth/token")
    Single<TokenResponse> login(@Body JsonObject jsonObject);

    @POST("user/register")
    Single<MessageResponse> signUp(@Body JsonObject jsonObject);

    @POST("auth/token")
    Call<TokenResponse> refreshToken(@Body JsonObject jsonObject);

    @GET("cart/list")
    Single<CartResponse> getCartItem(@Header("Authorization") String token);

    @POST("cart/add")
    Single<AddToCartResponse> addToCart(@Header("Authorization") String token , @Body JsonObject jsonObject);

    @POST("cart/remove")
    Single<MessageResponse> removeItemFromCart(@Header("Authorization") String token , @Body JsonObject jsonObject);

    @GET("cart/count")
    Single<CartItemCount> getCartItemCount(@Header("Authorization") String token);

    @POST("cart/changeCount")
    Single<AddToCartResponse> changeCount(@Header("Authorization") String token ,@Body JsonObject jsonObject);

    @POST("order/submit")
    Single<BilingResponse> submitOrder(@Header("Authorization") String token , @Body JsonObject jsonObject);

    @GET("order/checkout")
    Single<CheckoutResponse> checkOut(@Header("Authorization") String token , @Query("order_id") int orderId);

    @GET("/product/search")
    Single<List<Product>> search(@Query("q") String q);

}
