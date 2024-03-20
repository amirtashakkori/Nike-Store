package com.example.nikestore.ApiServices.ApiCommands;

import com.example.nikestore.ApiServices.ApiService;
import com.example.nikestore.Model.AddCommentResponse;
import com.example.nikestore.Model.Product;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class addCommentApi {

    ApiService apiService;

    public addCommentApi(ApiService apiService) {
        this.apiService = apiService;
    }

    public void leaveComment(){

    }

    public interface getProductCallBack{
        void onSubscribe(Disposable disposable);
        void onSuccess(AddCommentResponse commentResponse);
        void onError(Throwable e);
    }

}
