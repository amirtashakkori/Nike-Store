package com.example.nikestore.ApiServices.ApiCommands;

import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.nikestore.Adapters.ProductAdapter;
import com.example.nikestore.ApiServices.ApiService;
import com.example.nikestore.Model.Product;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class getProductApi {

    ApiService apiService;

    public getProductApi(ApiService apiService){
        this.apiService = apiService;
    }

    public void getProduct(String sort , getProductCallBack callBack){
        apiService.getProducts(sort).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Product>>() {
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
