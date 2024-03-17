package com.example.nikestore.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class CachedDatas {
    SharedPreferences cachedProduct;
    SharedPreferences cachedBanners;
    public CachedDatas(Context context){
        cachedProduct = context.getSharedPreferences("cached_products" , Context.MODE_PRIVATE);
        cachedBanners = context.getSharedPreferences("cached_banners" , Context.MODE_PRIVATE);
    }

    public void saveProducts(int id , String image , String title , int previous_price , int price){
        SharedPreferences.Editor editor = cachedProduct.edit();
        editor.putInt("id" , id);
        editor.putString("image" , image);
        editor.putString("title" , title);
        editor.putInt("previous_price" , previous_price);
        editor.putInt("price" , price);
        editor.apply();
    }

    public void saveBanners(String image){
        SharedPreferences.Editor editor = cachedBanners.edit();
        editor.putString("image" , image);
        editor.apply();
    }

    public int getId(){
        return cachedProduct.getInt("id" , 0);
    }

    public String getImage(){
        return cachedProduct.getString("image" , "");
    }

    public String getTitle(){
        return cachedProduct.getString("title" , "");
    }

    public int getPreviousPrice(){
        return cachedProduct.getInt("previous_price" , 0);
    }

    public int getPrice(){
        return cachedProduct.getInt("price" , 0);
    }

    public String getBanners(){
        return cachedBanners.getString("image" , "");

    }}
