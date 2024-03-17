package com.example.nikestore.ApiServices.ApiCommands;

import com.example.nikestore.ApiServices.ApiService;
import com.example.nikestore.Model.CheckoutResponse;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class checkout {
    ApiService apiService;

    public checkout(ApiService apiService) {
        this.apiService = apiService;
    }

    public void checkOut(String token , int order_id , checkOutCallBack callBack){
        apiService.checkOut(token , order_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<CheckoutResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onSubscribe(d);
            }

            @Override
            public void onSuccess(CheckoutResponse checkoutResponse) {
                callBack.onSuccess(checkoutResponse);
            }

            @Override
            public void onError(Throwable e) {
                callBack.onError(e);
            }
        });
    }

    public interface checkOutCallBack{
        void onSubscribe(Disposable d);
        void onSuccess(CheckoutResponse checkoutResponse);
        void onError(Throwable e);
    }
}
