package com.example.nikestore.ApiServices.ApiCommands;

import com.example.nikestore.ApiServices.ApiService;
import com.example.nikestore.Model.BilingResponse;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class submitOrder {
    ApiService apiService;

    public submitOrder(ApiService apiService) {
        this.apiService = apiService;
    }

    public void submitOrder(String token , String name , String family , String postalCode, String mobile , String address , String payment_method , submitOrderCalLBack calLBack){
        apiService.submitOrder(token , name , family , postalCode , mobile , address , payment_method).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<BilingResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                calLBack.onSubscribe(d);
            }

            @Override
            public void onSuccess(BilingResponse bilingResponse) {
                calLBack.onSuccess(bilingResponse);
            }

            @Override
            public void onError(Throwable e) {
                calLBack.onError(e);
            }
        });
    }

    public interface submitOrderCalLBack{
        void onSubscribe(Disposable disposable);
        void onSuccess(BilingResponse bilingResponse);
        void onError(Throwable throwable);
    }

}
