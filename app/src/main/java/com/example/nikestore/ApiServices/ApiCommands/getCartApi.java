package com.example.nikestore.ApiServices.ApiCommands;

import com.example.nikestore.ApiServices.ApiService;
import com.example.nikestore.Model.AddToCartResponse;
import com.example.nikestore.Model.CartItem;
import com.example.nikestore.Model.CartItemCount;
import com.example.nikestore.Model.CartResponse;
import com.example.nikestore.Model.MessageResponse;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class getCartApi {

    ApiService apiService;

    public getCartApi(ApiService apiService){
        this.apiService = apiService;
    }

    public void getCartItem(String token , getCartItemApiCallBack callBack){
        apiService.getCartItems(token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<CartResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onSubscribe(d);
            }

            @Override
            public void onSuccess(CartResponse cartResponse) {
                callBack.onSuccess(cartResponse);
            }

            @Override
            public void onError(Throwable e) {
                callBack.onError(e);
            }
        });
    }

    public void removeFromCart(String token , int CartItemId , removeItemCallBack callBack){
        apiService.removeItemFromCart(token , CartItemId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<MessageResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(MessageResponse messageResponse) {
                callBack.onSuccess(messageResponse);
            }

            @Override
            public void onError(Throwable e) {
                callBack.onError(e);
            }
        });
    }

    public void getCartItemCount(String token , getCartItemCountCallBack callBack){
        apiService.getCartItemCount(token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<com.example.nikestore.Model.CartItemCount>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(CartItemCount cartItemCount) {
                callBack.onSuccess(cartItemCount);
            }

            @Override
            public void onError(Throwable e) {
                callBack.onError(e);
            }
        });
    }

    public void changeCartItemCount(String token , int cartItemId , int count , changeCartItemCountCallBack callBack){
        apiService.changeCartItemCount(token , cartItemId , count).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<AddToCartResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onSubscribe(d);
            }

            @Override
            public void onSuccess(AddToCartResponse addToCartResponse) {
                callBack.onSuccess(addToCartResponse);
            }

            @Override
            public void onError(Throwable e) {
                callBack.onError(e);
            }
        });
    }

    public interface getCartItemApiCallBack{
        void onSubscribe(Disposable disposable);
        void onSuccess(CartResponse cartResponse);
        void onError(Throwable e);
    }

    public interface removeItemCallBack{
        void onSuccess(MessageResponse messageResponse);
        void onError(Throwable e);
    }

    public interface getCartItemCountCallBack{
        void onSuccess(CartItemCount cartItemCount);
        void onError(Throwable e);
    }

    public interface changeCartItemCountCallBack{
        void onSubscribe(Disposable disposable);
        void onSuccess(AddToCartResponse addToCartResponse);
        void onError(Throwable e);
    }

}
