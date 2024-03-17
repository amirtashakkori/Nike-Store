package com.example.nikestore.ApiServices.ApiCommands;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.nikestore.Adapters.BannerSliderAdapter;
import com.example.nikestore.ApiServices.ApiService;
import com.example.nikestore.Model.Banner;
import com.example.nikestore.Model.Product;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class getBannerApi{

    ApiService apiService;

    public getBannerApi(ApiService apiService){
        this.apiService = apiService;
    }

    public void getBanners(getBannerCallBack callBack){
        apiService.getBanners().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Banner>>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onSubscribe(d);
            }

            @Override
            public void onSuccess(List<Banner> banners) {
                callBack.onSuccess(banners);
            }

            @Override
            public void onError(Throwable e) {
                callBack.onError(e);
            }
        });

    }

    public interface getBannerCallBack{
        void onSubscribe(Disposable disposable);
        void onSuccess(List<Banner> banners);
        void onError(Throwable e);
    }

}
