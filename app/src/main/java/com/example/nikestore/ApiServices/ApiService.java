package com.example.nikestore.ApiServices;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
import com.example.nikestore.SharedPreferences.TokenContainer;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import io.reactivex.Single;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    TokenContainer tokenContainer;
    String token;
    RetrofitApiClass retrofitApiClass;
    public static final String BASE_URL = "http://expertdevelopers.ir/api/v1/";
    private int CLIENT_ID = 2;
    private String CLIENT_SECRET = "kyj1c9sVcksqGU4scMX7nLDalkjp2WoqQEf8PKAC";
    long cacheSize = (5 * 1024 * 1024);

    public boolean hasNetwork(Context context) {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected())
            isConnected = true;
        return isConnected;
    }

    public ApiService(Context context){
        Cache myCache = new Cache(context.getCacheDir(), cacheSize);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(myCache)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if (hasNetwork(context)){
                            request = request.newBuilder().addHeader("Cache-Control", "public, max-age=" + 5).build();
                        }
                        else{
                            request = request.newBuilder().addHeader(
                                    "Cache-Control",
                                    "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                            ).build();
                        }
                        return chain.proceed(request);
                    }
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        retrofitApiClass = retrofit.create(RetrofitApiClass.class);
    }

    public Single<List<Product>> getProducts(String sort){
        return retrofitApiClass.getProducts(sort);
    }

    public Single<List<Banner>> getBanners(){
        return retrofitApiClass.getBanners();
    }

    public Single<List<Comment>> getComments(Product product){
        return retrofitApiClass.getComments(product.getId());
    }

    public Single<AddCommentResponse> leaveComments(String token , Product product , String commentTitle , String commentContent){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("product_id" , product.id);
        jsonObject.addProperty("title" , commentTitle);
        jsonObject.addProperty("content" , commentContent);
        return retrofitApiClass.leaveComment(token , jsonObject);
    }

    public Single<OrderItemList> getOrderList(String token){
        return retrofitApiClass.getOrderList(token);
    }

    public Single<TokenResponse> login(String username , String password){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username" , username);
        jsonObject.addProperty("password" , password);
        jsonObject.addProperty("grant_type","password");
        jsonObject.addProperty("client_id", CLIENT_ID);
        jsonObject.addProperty("client_secret", CLIENT_SECRET);
        return retrofitApiClass.login(jsonObject);
    }

    public Single<MessageResponse> signUp(String username , String password){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email" , username);
        jsonObject.addProperty("password" , password);
        return retrofitApiClass.signUp(jsonObject);
    }

    public Single<CartResponse> getCartItems(String token){
        return retrofitApiClass.getCartItem(token);
    }

    public Single<AddToCartResponse> addToCart(String token , int productID){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("product_id" , productID);
        return retrofitApiClass.addToCart(token , jsonObject);
    }

    public Single<MessageResponse> removeItemFromCart(String token , int CartItemId){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("cart_item_id" , CartItemId);
        return retrofitApiClass.removeItemFromCart(token ,jsonObject);
    }

    public Single<AddToCartResponse> changeCartItemCount(String token , int CartItemId , int count){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("cart_item_id" , CartItemId);
        jsonObject.addProperty("count" , count);
        return retrofitApiClass.changeCount(token , jsonObject);
    }

    public Single<CartItemCount> getCartItemCount(String token){
        return retrofitApiClass.getCartItemCount(token);
    }

    public Single<BilingResponse> submitOrder(String token , String name , String family , String postalCode, String mobile , String address , String payment_method){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("first_name" , name);
        jsonObject.addProperty("last_name" , family);
        jsonObject.addProperty("postal_code" , postalCode);
        jsonObject.addProperty("mobile" , mobile);
        jsonObject.addProperty("address" , address);
        jsonObject.addProperty("payment_method" , payment_method);
        return retrofitApiClass.submitOrder(token , jsonObject);
    }

    public Single<CheckoutResponse> checkOut(String token , int order_id){
        return retrofitApiClass.checkOut(token , order_id);
    }

    public Single<List<Product>> search(String q){
        return retrofitApiClass.search(q);
    }

}
