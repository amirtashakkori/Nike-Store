package com.example.nikestore.ApiServices.ApiCommands;

import com.example.nikestore.ApiServices.ApiService;
import com.example.nikestore.Model.Product;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class search {
    ApiService apiService;

    public search(ApiService apiService){
        this.apiService = apiService;
    }

    public void search(String q , getProductCallBack callBack){
        apiService.search(q).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Product>>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onSubscribe(d);
            }

            @Override
            public void onSuccess(List<Product> products) {
                callBack.onSuccess(products);
            }

            @Override
            public void onError(Throwable e) {
                callBack.onError(e);
            }
        });
    }

    public interface getProductCallBack{
        void onSubscribe(Disposable disposable);
        void onSuccess(List<Product> products);
        void onError(Throwable e);
    }
}
